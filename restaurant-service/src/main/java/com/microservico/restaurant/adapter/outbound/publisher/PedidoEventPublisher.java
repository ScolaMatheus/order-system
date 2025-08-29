package com.microservico.restaurant.adapter.outbound.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.lib.orderEvents.event.PedidoStatusEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarPedidoCancelado(PedidoCanceladoEvent event) {
        kafkaTemplate.send("pedido-cancelado", event);
        log.info("PedidoCanceladoEvent publicado : {}", event);
    }

    public void publicarStatusPedido(PedidoStatusEvent event) {
        String topicName = event.getStatusPedido().equals(StatusPedido.PREPARANDO)
                ? "pedido-preparando" : "pedido-em-rota";

        kafkaTemplate.send(topicName, event);
        log.info("Pedido {} publicado com status {}", event.getPedidoId(), event.getStatusPedido().name());
    }
}