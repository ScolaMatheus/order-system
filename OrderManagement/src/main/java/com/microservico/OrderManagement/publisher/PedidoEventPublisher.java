package com.microservico.OrderManagement.publisher;

import com.microservico.OrderManagement.event.PedidoStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCriado(PedidoStatusEvent event) {
        rabbitTemplate.convertAndSend(
                PEDIDO_EXCHANGE,
                PEDIDO_ROUTING_KEY,
                event
        );

        log.info("Evento PedidoCriado {} enviado para exchange={} com routingKey={}",event ,PEDIDO_EXCHANGE, PEDIDO_ROUTING_KEY);
    }

}
