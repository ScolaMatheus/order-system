package com.microservico.restaurantService.publisher;

import com.microservico.restaurantService.event.PedidoCanceladoEvent;
import com.microservico.restaurantService.event.PedidoStatusEvent;
import com.microservico.restaurantService.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.microservico.restaurantService.util.RabbitConstants.*;

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

    public void publicarStatusPedido(StatusPedido statusPedido, PedidoStatusEvent event) {
        String routingKey = statusPedido.equals(StatusPedido.PREPARANDO)
                ? PEDIDO_PREPARANDO_ROUTING_KEY : PEDIDO_EM_ROTA_ROUTING_KEY;

        rabbitTemplate.convertAndSend(PEDIDO_EXCHANGE, routingKey, event);
        log.info("Pedido {} publicado com status {}", event.getPedidoId(), statusPedido.name());
    }
}
