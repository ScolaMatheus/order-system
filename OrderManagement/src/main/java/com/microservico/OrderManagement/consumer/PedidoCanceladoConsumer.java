package com.microservico.OrderManagement.consumer;

import com.microservico.OrderManagement.event.PedidoCanceladoEvent;
import com.microservico.OrderManagement.model.StatusPedido;
import com.microservico.OrderManagement.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Pedido cancelado recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

    }

}
