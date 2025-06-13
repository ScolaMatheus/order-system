package com.microservico.order.util;

public class RabbitConstants {

    public static final String PEDIDO_EXCHANGE = "pedido.status.exchange";
    
    public static final String PEDIDO_QUEUE = "pedido.status.criado.order.queue";
    public static final String PEDIDO_CANCELADO_QUEUE = "pedido.status.cancelado.order.queue";
    public static final String PEDIDO_PREPARANDO_QUEUE = "pedido.status.preparando.order.queue";
    public static final String PEDIDO_EM_ROTA_QUEUE = "pedido.status.em-rota.order.queue";
    public static final String PEDIDO_ENTREGUE_QUEUE = "pedido.status.entregue.order.queue";
    
    public static final String PEDIDO_ROUTING_KEY = "pedido.status.criado";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.status.cancelado";
    public static final String PEDIDO_PREPARANDO_ROUTING_KEY = "pedido.status.preparando";
    public static final String PEDIDO_EM_ROTA_ROUTING_KEY = "pedido.status.em-rota";
    public static final String PEDIDO_ENTREGUE_ROUTING_KEY = "pedido.status.entregue";

}
