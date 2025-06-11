package com.microservico.restaurantService.service;

import com.microservico.restaurantService.dto.response.PedidoDtoResponse;
import com.microservico.restaurantService.event.PedidoCanceladoEvent;
import com.microservico.restaurantService.event.PedidoStatusEvent;
import com.microservico.restaurantService.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurantService.exceptions.StatusIncorretoException;
import com.microservico.restaurantService.mapper.PedidoMapper;
import com.microservico.restaurantService.model.MenuItem;
import com.microservico.restaurantService.model.Pedido;
import com.microservico.restaurantService.model.Restaurant;
import com.microservico.restaurantService.publisher.PedidoEventPublisher;
import com.microservico.restaurantService.repositories.MenuItemRepository;
import com.microservico.restaurantService.repositories.PedidoRepository;
import com.microservico.restaurantService.repositories.RestaurantRepository;
import com.microservico.restaurantService.util.StatusPedido;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            this.salvarPedido(event);

            log.info("Pedido {} está sendo preparado pelo restaurante {}", event.getPedidoId(), event.getRestauranteId());
            this.publicarPedido(event, StatusPedido.PREPARANDO);

        } else {
            log.warn("Pedido {} CANCELADO - restaurante ou item inválido/inativado", event.getPedidoId());
            this.processarCancelamentoDePedido(event);
        }
    }

    @Transactional
    public PedidoStatusEvent informarPedidoPronto(Long idPedido) {
        // Buscar pedido.
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Não foi encontrado nenhum pedido para esse id: " + idPedido)
        );

        // Atualizando status do pedido na base.
        if (pedido.getStatusPedido() == StatusPedido.PREPARANDO) {
            pedido.setStatusPedido(StatusPedido.EM_ROTA);
            pedidoRepository.save(pedido);
        } else {
            throw new StatusIncorretoException("O pedido: " + idPedido + " não está com o status correto para colocar o pedido em rota. Status atual: " + pedido.getStatusPedido().name());
        }

        PedidoStatusEvent pedidoEvent = PedidoMapper.entityToEvent(pedido);

        this.publicarPedido(pedidoEvent, StatusPedido.EM_ROTA);

        return pedidoEvent;
    }

    public List<PedidoDtoResponse> buscarPedidoPorRestaurante(Long idRestaurante) {
        return pedidoRepository.findByRestauranteId(idRestaurante).stream()
                .map(PedidoMapper::toDto).toList();
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

        if (!restaurant.isAtivo()) {
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

    private void publicarPedido(PedidoStatusEvent event, StatusPedido statusPedido) {
        pedidoEventPublisher.publicarStatusPedido(statusPedido, event);
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
                "restaurant-service"
        ));
    }
}
