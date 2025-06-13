package com.microservico.order.consumer;

import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.order.util.RabbitConstants.PEDIDO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_QUEUE)
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido recebido: {}",  event);
        pedidoService.criarPedido(event);
    }
}
