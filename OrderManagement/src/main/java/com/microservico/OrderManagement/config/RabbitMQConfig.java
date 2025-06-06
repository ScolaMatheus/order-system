package com.microservico.OrderManagement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PEDIDO_EXCHANGE = "pedido.exchange";
    public static final String PEDIDO_QUEUE = "pedido.criado.queue";
    public static final String PEDIDO_ROUTING_KEY = "pedido.criado";
    public static final String PEDIDO_CANCELADO_QUEUE = "pedido.cancelado.queue";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.cancelado";

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // Conversor de objeto -> JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Template com conversor JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter);
        template.setMandatory(true);

        template.setBeforePublishPostProcessors(message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });

        return template;

    }

    // Exchange do tipo direct
    @Bean
    public DirectExchange pedidoExchange() {
        return new DirectExchange(PEDIDO_EXCHANGE);
    }

    // Fila que vai receber os eventos do pedido criado
    @Bean
    public Queue pedidoQueue() {
        return QueueBuilder.durable(PEDIDO_QUEUE).build();
    }

    // Fila que vai receber os eventos dos pedidos cancelados
    @Bean
    public Queue pedidoCanceladoQueue() {
        return QueueBuilder.durable(PEDIDO_CANCELADO_QUEUE).build();
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue)
                .to(pedidoExchange)
                .with(PEDIDO_ROUTING_KEY);
    }

    @Bean
    public Binding pedidoCanceladoBinding(Queue pedidoCanceladoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoCanceladoQueue)
                .to(pedidoExchange)
                .with(PEDIDO_CANCELADO_ROUTING_KEY);
    }

}
