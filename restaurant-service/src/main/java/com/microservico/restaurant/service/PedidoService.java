package com.microservico.restaurant.service;

import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.event.PedidoCanceladoEvent;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.exceptions.StatusIncorretoException;
import com.microservico.restaurant.mapper.PedidoMapper;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Pedido;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.publisher.PedidoEventPublisher;
import com.microservico.restaurant.repositories.MenuItemRepository;
import com.microservico.restaurant.repositories.PedidoRepository;
import com.microservico.restaurant.repositories.RestaurantRepository;
import com.microservico.restaurant.util.StatusPedido;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.microservico.restaurant.util.OrigemCancelamento.RESTAURANT_SERVICE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final PedidoEventPublisher pedidoEventPublisher;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public void consumirPedido(PedidoStatusEvent event) {
        boolean pedidoValido = validarRestautanteEItens(event.getRestauranteId(), event.getItens());

        if (pedidoValido) {
            log.info("Pedido {} aceito pelo restaurante {}", event.getPedidoId(), event.getRestauranteId());
            event.setStatusPedido(StatusPedido.PREPARANDO);
            this.salvarPedido(event);

            log.info("Pedido {} está sendo preparado pelo restaurante {}", event.getPedidoId(), event.getRestauranteId());
            this.publicarPedido(event);

        } else {
            log.warn("Pedido {} CANCELADO - restaurante ou item inválido/inativado", event.getPedidoId());
            this.processarCancelamentoDePedido(event);
        }
    }

    @Transactional
    public PedidoStatusEvent informarPedidoPronto(Long idPedido) {
        // Buscar pedido.
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> {
                    log.error("Pedido não encontrado com esse id: {}", idPedido);
                    return new RecursoNaoEncontradoException("Não foi encontrado nenhum pedido para esse id: " + idPedido);
                });

        // Atualizando status do pedido na base.
        if (pedido.getStatusPedido() == StatusPedido.PREPARANDO) {
            pedido.setStatusPedido(StatusPedido.EM_ROTA);
            pedidoRepository.save(pedido);
        } else {
            log.error("O pedido {} não está com o status correto para colocar o pedido em rota. Status atual: {}", pedido.getId(), pedido.getStatusPedido().name());
            throw new StatusIncorretoException("O pedido: " + idPedido + " não está com o status correto para colocar o pedido em rota. Status atual: " + pedido.getStatusPedido().name());
        }

        PedidoStatusEvent pedidoEvent = PedidoMapper.entityToEvent(pedido);

        this.publicarPedido(pedidoEvent);
        log.info("Pedido {} está em rota de entrega", pedidoEvent.getPedidoId());

        return pedidoEvent;
    }

    public List<PedidoDtoResponse> buscarPedidoPorRestaurante(Long idRestaurante) {
        return pedidoRepository.findByRestauranteId(idRestaurante).stream()
                .map(PedidoMapper::toDto).toList();
    }

    @Transactional
    public void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() != StatusPedido.CANCELADO) {
            pedido.setStatusPedido(statusPedido);
            pedido.setDataAtualizacao(dataHoraAtualizacao);
        }

        pedidoRepository.save(pedido);

        log.info("Pedido {} alterado para status {} às {}",idPedido, statusPedido, dataHoraAtualizacao);
    }

    private void salvarPedido(PedidoStatusEvent event) {
        Pedido pedido = PedidoMapper.eventToEntity(event);
        pedidoRepository.save(pedido);
    }

    private boolean validarRestautanteEItens(Long idRestaurant, List<PedidoStatusEvent.ItemPedidoEvent> itens) {

        Restaurant restaurant = restaurantRepository.findById(idRestaurant).orElse(null);

        if (Objects.equals(restaurant, null)) {
            log.warn("Restaurant {} não encontrado!", idRestaurant);
            return false;
        }

        if (!restaurant.getAtivo()) {
            log.warn("Restaurante {} está inativado!", idRestaurant);
            return false;
        }

        boolean algumItemInvalido = itens.stream().anyMatch(item -> {
            Optional<MenuItem> menuItem = menuItemRepository.findById(item.getProdutoId());

            if (menuItem.isEmpty()) {
                log.warn("Item {} não encontrado!", item.getProdutoId());
                return true;
            }

            if (!menuItem.get().getAtivo()) {
                log.warn("Item {} está inativo", item.getProdutoId());
                return true;
            }
            return false;
        });

        return !algumItemInvalido;
    }

    private void publicarPedido(PedidoStatusEvent event) {
        pedidoEventPublisher.publicarStatusPedido(event);
    }

    private void processarCancelamentoDePedido(PedidoStatusEvent event) {
        pedidoRepository.findById(event.getPedidoId()) // Valida se o pedido existe no banco.
                .ifPresentOrElse(pedido -> {
                    if (pedido.getStatusPedido() != StatusPedido.CANCELADO) { // Caso ele exista, altero o status dele para cancelado e atualizo no banco.
                        pedido.setStatusPedido(StatusPedido.CANCELADO);
                        pedidoRepository.save(pedido);
                    }
                }, () -> {
                    event.setStatusPedido(StatusPedido.CANCELADO); // Caso ele não exista no banco, altero o valor do Status do event, converto ele num pedido e salvo no banco.
                    pedidoRepository.save(PedidoMapper.eventToEntity(event));
                });

        pedidoEventPublisher.publicarPedidoCancelado(new PedidoCanceladoEvent(
                event.getPedidoId(),
                event.getRestauranteId(),
                event.getClienteId(),
                StatusPedido.CANCELADO,
                LocalDateTime.now(),
                "Pedido cancelado devido a Restaurante ou item inválido/inativado",
                RESTAURANT_SERVICE.name()
        ));
    }
}
