package com.microservico.restaurantService.publisher;

import com.microservico.restaurantService.event.PedidoCanceladoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCancelado(PedidoCanceladoEvent event) {
        rabbitTemplate.convertAndSend(
                "pedido.exchange",
                "pedido.cancelado",
                event
        );
        log.info("PedidoCanceladoEvent publicado : {}", event);
    }
}
