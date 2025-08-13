package com.microservico.customer.application.consumer;

import com.microservico.customer.event.PedidoStatusEvent;

public interface IPedidoPreparandoConsumer {

    void consumirPedidoEmPreparo (PedidoStatusEvent event);

}
