package com.microservico.restaurant.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.microservico.restaurant.util.RabbitConstants.*;

@Configuration
public class RabbitMQConfig {

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

    // Exchange do tipo topic
    @Bean
    public TopicExchange pedidoExchange() {
        return new TopicExchange(PEDIDO_EXCHANGE);
    }

    // Fila que vai receber os eventos do pedido criado
    @Bean
    public Queue pedidoQueue() {
        return definirFila(PEDIDO_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos cancelados
    @Bean
    public Queue pedidoCanceladoQueue() {
        return definirFila(PEDIDO_CANCELADO_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos entregues
    @Bean
    public Queue pedidoEntregueQueue() {
        return definirFila(PEDIDO_ENTREGUE_QUEUE);
    }

    // Ligação entre a exchange e a fila com uma routing key (pedidos criados)
    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoQueue, pedidoExchange, PEDIDO_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key (pedidos cancelados)
    @Bean
    public Binding pedidoCanceladoBinding(Queue pedidoCanceladoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoCanceladoQueue, pedidoExchange, PEDIDO_CANCELADO_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key (pedidos entregues)
    @Bean
    public Binding pedidoEntregueBinding(Queue pedidoEntregueQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoEntregueQueue, pedidoExchange, PEDIDO_ENTREGUE_ROUTING_KEY);
    }

    private Queue definirFila(String nameQueue) {
        return QueueBuilder.durable(nameQueue).build();
    }

    private Binding definirBindings(Queue queue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder.bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }

}
