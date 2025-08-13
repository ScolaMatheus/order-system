package com.microservico.customer.application.consumer;

import com.microservico.customer.event.PedidoStatusEvent;

public interface IPedidoEmRotaConsumer {

    void consumirPedidoEmRota(PedidoStatusEvent event);

}
