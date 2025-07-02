package com.microservico.customer.service;

import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.exceptions.RecursoNaoEncontradoException;
import com.microservico.customer.exceptions.StatusIncorretoException;
import com.microservico.customer.mapper.PedidoMapper;
import com.microservico.customer.model.Pedido;
import com.microservico.customer.publisher.PedidoEventPublisher;
import com.microservico.customer.repositories.PedidoRepository;
import com.microservico.customer.util.StatusPedido;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.microservico.customer.util.OrigemCancelamento.CUSTOMER_SERVICE;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {

    private final PedidoEventPublisher pedidoEventPublisher;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public PedidoDtoResponse criarPedidoEvent(PedidoDtoRequest request) {
        // Converter o Pedido
        Pedido pedido = PedidoMapper.toEntity(request);

        // Salvar o Pedido no banco
        pedido = pedidoRepository.save(pedido);

        // Converter o Pedido em Event
        PedidoStatusEvent statusEvent = PedidoMapper.entityToEvent(pedido);

        // Publicar a criação do Pedido
        pedidoEventPublisher.publicarPedidoCriado(statusEvent);

        // Retornar o valor para a controller
        return PedidoMapper.toDto(pedido);
    }

    @Transactional
    public PedidoStatusEvent informarPedidoEntregue(Long idPedido) {
        // Buscar pedido.
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Não foi encontrado nenhum pedido para esse id: " + idPedido)
        );

        // Validando se o pedido está com o status correto
        if (pedido.getStatusPedido() == StatusPedido.EM_ROTA) {
            // Atualizando status do pedido na base.
            pedido.setStatusPedido(StatusPedido.ENTREGUE);
            pedidoRepository.save(pedido);
        } else {
            throw new StatusIncorretoException("O pedido: " + idPedido + " não está com o status correto para colocar o pedido como entregue. Status atual: " + pedido.getStatusPedido().name());
        }

        PedidoStatusEvent pedidoEvent = PedidoMapper.entityToEvent(pedido);

        pedidoEventPublisher.publicarPedidoEntregue(pedidoEvent);

        return pedidoEvent;
    }

    @Transactional
    public void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO) {
            log.info("Pedido {} não foi alterado pois já está cancelado.", idPedido);
            return;
        }

        pedido.setStatusPedido(statusPedido);
        pedido.setDataAtualizacao(dataHoraAtualizacao);

        pedidoRepository.save(pedido);

        log.info("Pedido {} alterado para status {} às {}",idPedido, statusPedido, dataHoraAtualizacao);
    }

    public PedidoCanceladoEvent processarCancelamentoDePedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() != StatusPedido.CANCELADO && pedido.getStatusPedido() != StatusPedido.EM_ROTA) {
            pedido.setStatusPedido(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
        } else {
            throw new StatusIncorretoException(
                    "O pedido: " + idPedido + " não pode ser cancelado pois ele já foi cancelado ou está a caminho da entrega. " +
                            "Status atual: " + pedido.getStatusPedido().name()
            );
        }

        PedidoCanceladoEvent canceladoEvent = new PedidoCanceladoEvent(
                pedido.getId(),
                pedido.getRestauranteId(),
                pedido.getClienteId(),
                StatusPedido.CANCELADO,
                LocalDateTime.now(),
                "Pedido cancelado por decisão do cliente",
                CUSTOMER_SERVICE.name()
        );

        pedidoEventPublisher.publicarPedidoCancelado(canceladoEvent);

        return canceladoEvent;
    }
}
