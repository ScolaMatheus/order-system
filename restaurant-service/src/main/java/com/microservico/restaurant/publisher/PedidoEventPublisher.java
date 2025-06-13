package com.microservico.restaurant.publisher;

import com.microservico.restaurant.event.PedidoCanceladoEvent;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.RabbitConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCancelado(PedidoCanceladoEvent event) {
        rabbitTemplate.convertAndSend(
                PEDIDO_EXCHANGE,
                PEDIDO_CANCELADO_ROUTING_KEY,
                event
        );
        log.info("PedidoCanceladoEvent publicado : {}", event);
    }

    public void publicarStatusPedido(PedidoStatusEvent event) {
        String routingKey = event.getStatusPedido().equals(StatusPedido.PREPARANDO)
                ? PEDIDO_PREPARANDO_ROUTING_KEY : PEDIDO_EM_ROTA_ROUTING_KEY;

        rabbitTemplate.convertAndSend(PEDIDO_EXCHANGE, routingKey, event);
        log.info("Pedido {} publicado com status {}", event.getPedidoId(), event.getStatusPedido().name());
    }
}
