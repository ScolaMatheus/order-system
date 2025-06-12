package com.microservico.customerservice.publisher;

import com.microservico.customerservice.event.PedidoCanceladoEvent;

import com.microservico.customerservice.event.PedidoStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.microservico.customerservice.util.RabbitConstants.*;


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

}
