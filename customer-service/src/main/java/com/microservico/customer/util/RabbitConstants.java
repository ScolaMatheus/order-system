package com.microservico.customer.util;

public class RabbitConstants {

    public static final String PEDIDO_EXCHANGE = "pedido.status.exchange";

    public static final String PEDIDO_QUEUE = "pedido.status.criado.customer.queue";
    public static final String PEDIDO_CANCELADO_QUEUE = "pedido.status.cancelado.customer.queue";
    public static final String PEDIDO_PREPARANDO_QUEUE = "pedido.status.preparando.customer.queue";
    public static final String PEDIDO_EM_ROTA_QUEUE = "pedido.status.em-rota.customer.queue";

    public static final String PEDIDO_ROUTING_KEY = "pedido.status.criado";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.status.cancelado";
    public static final String PEDIDO_PREPARANDO_ROUTING_KEY = "pedido.status.preparando";
    public static final String PEDIDO_EM_ROTA_ROUTING_KEY = "pedido.status.em-rota";
    public static final String PEDIDO_ENTREGUE_ROUTING_KEY = "pedido.status.entregue";
}
