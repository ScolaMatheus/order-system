package com.microservico.customerservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.microservico.customerservice.util.RabbitConstants.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Template para conversor JSON
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

    @Bean
    public Binding pedidoCanceladoBinding(Queue pedidoCanceladoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoCanceladoQueue, pedidoExchange, PEDIDO_CANCELADO_ROUTING_KEY);
    }
    @Bean
    public Binding pedidoPreparandoBinding(Queue pedidoPreparandoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoPreparandoQueue, pedidoExchange, PEDIDO_PREPARANDO_ROUTING_KEY);
    }
    @Bean
    public Binding pedidoEmRotaBinding(Queue pedidoEmRotaQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoEmRotaQueue, pedidoExchange, PEDIDO_EM_ROTA_ROUTING_KEY);
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
