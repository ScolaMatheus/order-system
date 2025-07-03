package com.microservico.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.microservico.order.util.RabbitConstants.*;

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

    // Exchange do tipo topic
    @Bean
    public TopicExchange pedidoExchange() {
        return new TopicExchange(PEDIDO_EXCHANGE);
    }

    // Exchange do tipo topic reprocessar as mensagens que falharam
    @Bean
    public TopicExchange pedidoExchangeDlx() {
        return new TopicExchange(PEDIDO_EXCHANGE_DLX);
    }

    // Fila que vai receber os eventos do pedido criado
    @Bean
    public Queue pedidoCriadoQueue() {
        return definirFilaComDlx(PEDIDO_CRIADO_QUEUE, PEDIDO_CRIADO_RETRY_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos criados que falharam no consumo
    @Bean
    public Queue pedidoCriadoQueueDlq() {
        return definirFilaRetry(PEDIDO_CRIADO_QUEUE_DLQ, PEDIDO_CRIADO_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos cancelados
    @Bean
    public Queue pedidoCanceladoQueue() {
        return definirFilaComDlx(PEDIDO_CANCELADO_QUEUE, PEDIDO_CANCELADO_RETRY_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos cancelados que falharam no consumo
    @Bean
    public Queue pedidoCanceladoQueueDlq() {
        return definirFilaRetry(PEDIDO_CANCELADO_QUEUE_DLQ, PEDIDO_CANCELADO_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos em preparação
    @Bean
    public Queue pedidoPreparandoQueue() {
        return definirFilaComDlx(PEDIDO_PREPARANDO_QUEUE, PEDIDO_PREPARANDO_RETRY_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos em preparação que falharam no consumo
    @Bean
    public Queue pedidoPreparandoQueueDlq() {
        return definirFilaRetry(PEDIDO_PREPARANDO_QUEUE_DLQ, PEDIDO_PREPARANDO_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos em rota de entrega
    @Bean
    public Queue pedidoEmRotaQueue() {
        return definirFilaComDlx(PEDIDO_EM_ROTA_QUEUE, PEDIDO_EM_ROTA_RETRY_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos em rota que falharam no consumo
    @Bean
    public Queue pedidoEmRotaQueueDlq() {
        return definirFilaRetry(PEDIDO_EM_ROTA_QUEUE_DLQ, PEDIDO_EM_ROTA_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos entregues
    @Bean
    public Queue pedidoEntregueQueue() {
        return definirFilaComDlx(PEDIDO_ENTREGUE_QUEUE, PEDIDO_ENTREGUE_RETRY_ROUTING_KEY);
    }

    // Fila que vai receber os eventos dos pedidos entregues que falharam no consumo
    @Bean
    public Queue pedidoEntregueQueueDlq() {
        return definirFilaRetry(PEDIDO_ENTREGUE_QUEUE_DLQ, PEDIDO_ENTREGUE_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoCriadoBinding(Queue pedidoCriadoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoCriadoQueue, pedidoExchange, PEDIDO_CRIADO_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key para caso de falha no consumo
    @Bean
    public Binding pedidoCriadoBindingDlq(Queue pedidoCriadoQueueDlq, TopicExchange pedidoExchangeDlx) {
        return definirBindings(pedidoCriadoQueueDlq, pedidoExchangeDlx, PEDIDO_CRIADO_RETRY_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoCanceladoBinding(Queue pedidoCanceladoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoCanceladoQueue, pedidoExchange, PEDIDO_CANCELADO_ROUTING_KEY_ALL);
    }

    // Ligação entre a exchange e a fila com uma routing key para caso de falhas no consumo
    @Bean
    public Binding pedidoCanceladoBindingDlq(Queue pedidoCanceladoQueueDlq, TopicExchange pedidoExchangeDlx) {
        return definirBindings(pedidoCanceladoQueueDlq, pedidoExchangeDlx, PEDIDO_CANCELADO_RETRY_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoPreparandoBinding(Queue pedidoPreparandoQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoPreparandoQueue, pedidoExchange, PEDIDO_PREPARANDO_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key para caso de falhas no consumo
    @Bean
    public Binding pedidoPreparandoBindingDlq(Queue pedidoPreparandoQueueDlq, TopicExchange pedidoExchangeDlx) {
        return definirBindings(pedidoPreparandoQueueDlq, pedidoExchangeDlx, PEDIDO_PREPARANDO_RETRY_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoEmRotaBinding(Queue pedidoEmRotaQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoEmRotaQueue, pedidoExchange, PEDIDO_EM_ROTA_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key para caso de falhas no consumo
    @Bean
    public Binding pedidoEmRotaBindingDlq(Queue pedidoEmRotaQueueDlq, TopicExchange pedidoExchangeDlx) {
        return definirBindings(pedidoEmRotaQueueDlq, pedidoExchangeDlx, PEDIDO_EM_ROTA_RETRY_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key
    @Bean
    public Binding pedidoEntregueBinding(Queue pedidoEntregueQueue, TopicExchange pedidoExchange) {
        return definirBindings(pedidoEntregueQueue, pedidoExchange, PEDIDO_ENTREGUE_ROUTING_KEY);
    }

    // Ligação entre a exchange e a fila com uma routing key para caso de falhas no consumo
    @Bean
    public Binding pedidoEntregueBindingDlq(Queue pedidoEntregueQueueDlq, TopicExchange pedidoExchangeDlx) {
        return definirBindings(pedidoEntregueQueueDlq, pedidoExchangeDlx, PEDIDO_ENTREGUE_RETRY_ROUTING_KEY);
    }

    private Queue definirFilaComDlx(String nameQueue, String retryRoutingKey) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", PEDIDO_EXCHANGE_DLX);
        args.put("x-dead-letter-routing-key", retryRoutingKey);

        return QueueBuilder.durable(nameQueue).withArguments(args).build();
    }

    private Queue definirFilaRetry(String retryQueueName, String nameRoutingKey) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 8000);
        args.put("x-dead-letter-exchange", PEDIDO_EXCHANGE);
        args.put("x-dead-letter-routing-key", nameRoutingKey);
        return QueueBuilder.durable(retryQueueName).withArguments(args).build();
    }

    private Binding definirBindings(Queue queue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder.bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }

}
