package com.microservico.customer.application.consumer;

import org.lib.orderEvents.event.PedidoStatusEvent;

public interface IPedidoPreparandoConsumer {

    void consumirPedidoEmPreparo (PedidoStatusEvent event);

}
