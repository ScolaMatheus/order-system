package com.microservico.customer.application.publisher;


import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.lib.orderEvents.event.PedidoStatusEvent;

public interface IPedidoEventPublisher {

    void publicarPedidoCancelado(PedidoCanceladoEvent event);

    void publicarPedidoEntregue(PedidoStatusEvent event);

    void publicarPedidoCriado(PedidoStatusEvent pedidoEvent);


}
