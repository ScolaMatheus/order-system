package com.microservico.customer.application.consumer;

import com.microservico.customer.event.PedidoCanceladoEvent;

public interface IPedidoCanceladoConsumer {

    void consumirPedidoCancelado(PedidoCanceladoEvent event);

}
