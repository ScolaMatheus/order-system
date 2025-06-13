package com.microservico.customer.publisher;

import com.microservico.customer.event.PedidoCanceladoEvent;

import com.microservico.customer.event.PedidoStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.microservico.customer.util.RabbitConstants.*;


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

    public void publicarPedidoEntregue(PedidoStatusEvent pedidoEvent) {
        rabbitTemplate.convertAndSend(
                PEDIDO_EXCHANGE,
                PEDIDO_ENTREGUE_ROUTING_KEY,
                pedidoEvent
        );
        log.info("PedidoStatusEvent {} foi entregue ao cliente {}", pedidoEvent, pedidoEvent.getClienteId());
    }

    public void publicarPedidoCriado(PedidoStatusEvent pedidoEvent) {
        rabbitTemplate.convertAndSend(
                PEDIDO_EXCHANGE,
                PEDIDO_ROUTING_KEY,
                pedidoEvent
        );
        log.info("PedidoStatusEvent {} foi criado no customer-service", pedidoEvent);
    }
}
