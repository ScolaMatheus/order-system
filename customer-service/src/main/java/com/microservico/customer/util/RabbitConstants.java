package com.microservico.customer.util;

public class RabbitConstants {

    // Nome das Exchanges
    public static final String PEDIDO_EXCHANGE = "pedido.status.exchange";
    public static final String PEDIDO_EXCHANGE_DLX = "pedido.status.exchange.dlx";

    // Nome das filas
    public static final String PEDIDO_CRIADO_QUEUE = "pedido.status.criado.customer.queue";
    public static final String PEDIDO_CANCELADO_QUEUE = "pedido.status.cancelado.customer.queue";
    public static final String PEDIDO_PREPARANDO_QUEUE = "pedido.status.preparando.customer.queue";
    public static final String PEDIDO_EM_ROTA_QUEUE = "pedido.status.em-rota.customer.queue";

    // Nome das filas para processar as falhas do consumo das filas originais
    public static final String PEDIDO_CANCELADO_QUEUE_DLQ = "pedido.status.cancelado.customer.queue.dlq";
    public static final String PEDIDO_PREPARANDO_QUEUE_DLQ = "pedido.status.preparando.customer.queue.dlq";
    public static final String PEDIDO_EM_ROTA_QUEUE_DLQ = "pedido.status.em-rota.customer.queue.dlq";

    // Palavras-chave para as filas
    public static final String PEDIDO_CRIADO_ROUTING_KEY = "pedido.status.criado";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY_CUSTOMER = "pedido.status.cancelado.customer";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY_ALL = "pedido.status.cancelado.*";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.status.cancelado";
    public static final String PEDIDO_PREPARANDO_ROUTING_KEY = "pedido.status.preparando";
    public static final String PEDIDO_EM_ROTA_ROUTING_KEY = "pedido.status.em-rota";
    public static final String PEDIDO_ENTREGUE_ROUTING_KEY = "pedido.status.entregue";

    // Palavras-chave para as filas de retry
    public static final String PEDIDO_CANCELADO_RETRY_ROUTING_KEY = "pedido.status.cancelado.retry";
    public static final String PEDIDO_PREPARANDO_RETRY_ROUTING_KEY = "pedido.status.preparando.retry";
    public static final String PEDIDO_EM_ROTA_RETRY_ROUTING_KEY = "pedido.status.em-rota.retry";
    public static final String PEDIDO_ENTREGUE_RETRY_ROUTING_KEY = "pedido.status.entregue.retry";
}
