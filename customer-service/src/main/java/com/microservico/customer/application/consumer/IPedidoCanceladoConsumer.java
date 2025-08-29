package com.microservico.customer.application.consumer;


import org.lib.orderEvents.event.PedidoCanceladoEvent;

public interface IPedidoCanceladoConsumer {

    void consumirPedidoCancelado(PedidoCanceladoEvent event);

}
