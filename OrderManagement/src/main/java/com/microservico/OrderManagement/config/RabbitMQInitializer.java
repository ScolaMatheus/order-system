package com.microservico.OrderManagement.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rabbit.init.enabled", havingValue = "true")
public class RabbitMQInitializer {

    private final AmqpAdmin amqpAdmin;
    private final Queue pedidoQueue;
    private final TopicExchange pedidoExchange;
    private final Binding pedidoBinding;

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(pedidoQueue);
        amqpAdmin.declareExchange(pedidoExchange);
        amqpAdmin.declareBinding(pedidoBinding);
    }

}
