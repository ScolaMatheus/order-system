package com.microservico.restaurantService.util;

public class RabbitConstants {

    public static final String PEDIDO_EXCHANGE = "pedido.exchange";
    public static final String PEDIDO_QUEUE = "pedido.criado.queue";
    public static final String PEDIDO_PREPARANDO_QUEUE = "pedido.preparando.queue";
    public static final String PEDIDO_EM_ROTA_QUEUE = "pedido.em-rota.queue";
    public static final String PEDIDO_ENTREGE_QUEUE = "pedido.entrege.queue";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.cancelado";
    public static final String PEDIDO_PREPARANDO_ROUTING_KEY = "pedido.preparando";
    public static final String PEDIDO_EM_ROTA_ROUTING_KEY = "pedido.em-rota";
    public static final String PEDIDO_ENTREGE_ROUTING_KEY = "pedido.entrege";

}
