package com.microservico.customer.application.publisher;

import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.event.PedidoStatusEvent;

public interface IPedidoEventPublisher {

    void publicarPedidoCancelado(PedidoCanceladoEvent event);

    void publicarPedidoEntregue(PedidoStatusEvent event);

    void publicarPedidoCriado(PedidoStatusEvent pedidoEvent);


}
