package com.microservico.customer.application.consumer;

import org.lib.orderEvents.event.PedidoStatusEvent;

public interface IPedidoEmRotaConsumer {

    void consumirPedidoEmRota(PedidoStatusEvent event);

}
