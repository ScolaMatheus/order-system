package com.microservico.restaurantService.consumer;

import com.microservico.restaurantService.event.PedidoStatusEvent;
import com.microservico.restaurantService.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurantService.util.RabbitConstants.PEDIDO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_QUEUE)
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido recebido: {}",  event);
        pedidoService.consumirPedido(event);
    }
}
