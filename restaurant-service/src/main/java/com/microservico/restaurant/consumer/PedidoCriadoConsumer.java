package com.microservico.restaurant.consumer;

import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.RabbitConstants.PEDIDO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_QUEUE)
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido recebido: {}",  event);
        pedidoService.consumirPedido(event);
    }
}
