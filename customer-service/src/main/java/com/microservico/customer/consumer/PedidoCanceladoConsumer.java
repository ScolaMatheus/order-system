package com.microservico.customer.consumer;

import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.service.PedidoService;
import com.microservico.customer.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.customer.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Pedido cancelado recebido: {}", event);
        try {
            pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

        } catch (Exception e) {
            log.error("Erro ao processar evento de pedido [{}]: {}", event.getPedidoId(), e.getMessage(), e);
        }

    }

}
