package com.microservico.OrderManagement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.microservico.OrderManagement.util.RabbitConstants.*;

@Configuration
public class RabbitMQConfig {

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
        return definirFila(PEDIDO_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos cancelados
    @Bean
    public Queue pedidoCanceladoQueue() {
        return definirFila(PEDIDO_CANCELADO_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos em preparação
    @Bean
    public Queue pedidoPreparandoQueue() {
        return definirFila(PEDIDO_PREPARANDO_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos em rota de entrega
    @Bean
    public Queue pedidoEmRotaQueue() {
        return definirFila(PEDIDO_EM_ROTA_QUEUE);
    }

    // Fila que vai receber os eventos dos pedidos entregues
    @Bean
    public Queue pedidoEntregeQueue() {
        return definirFila(PEDIDO_ENTREGE_QUEUE);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange pedidoExchange) {
        return definirBindinds(pedidoQueue, pedidoExchange, PEDIDO_ROUTING_KEY);
    }

    @Bean
    public Binding pedidoCanceladoBinding(Queue pedidoCanceladoQueue, DirectExchange pedidoExchange) {
        return definirBindinds(pedidoCanceladoQueue, pedidoExchange, PEDIDO_CANCELADO_ROUTING_KEY);
    }

    @Bean
    public Binding pedidoPreparandoBinding(Queue pedidoPreparandoQueue, DirectExchange pedidoExchange) {
        return definirBindinds(pedidoPreparandoQueue, pedidoExchange, PEDIDO_PREPARANDO_ROUTING_KEY);
    }

    @Bean
    public Binding pedidoEmRotaBinding(Queue pedidoEmRotaQueue, DirectExchange pedidoExchange) {
        return definirBindinds(pedidoEmRotaQueue, pedidoExchange, PEDIDO_EM_ROTA_ROUTING_KEY);
    }

    @Bean
    public Binding pedidoEntregeBinding(Queue pedidoEntregeQueue, DirectExchange pedidoExchange) {
        return definirBindinds(pedidoEntregeQueue, pedidoExchange, PEDIDO_ENTREGE_ROUTING_KEY);
    }

    private Queue definirFila(String nameQueue) {
        return QueueBuilder.durable(nameQueue).build();
    }

    private Binding definirBindinds(Queue queue, DirectExchange directExchange, String routingKey) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(routingKey);
    }

}
