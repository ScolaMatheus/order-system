package com.microservico.customer.adapter.outbound.publisher;

import com.microservico.customer.application.publisher.IPedidoEventPublisher;
import com.microservico.customer.event.PedidoCanceladoEvent;

import com.microservico.customer.event.PedidoStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PedidoEventPublisher implements IPedidoEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PedidoEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publicarPedidoCancelado(PedidoCanceladoEvent event) {
        kafkaTemplate.send("pedido-cancelado-group", event);
        log.info("PedidoCanceladoEvent publicado : {}", event);
    }

    @Override
    public void publicarPedidoEntregue(PedidoStatusEvent pedidoEvent) {
        kafkaTemplate.send("pedido-entregue-group", pedidoEvent);
        log.info("PedidoStatusEvent {} foi entregue ao cliente {}", pedidoEvent, pedidoEvent.getClienteId());
    }

    @Override
    public void publicarPedidoCriado(PedidoStatusEvent pedidoEvent) {
        kafkaTemplate.send("pedido-criado-group", pedidoEvent);
        log.info("PedidoStatusEvent {} foi criado no customer-service", pedidoEvent);
    }
}
