package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoCanceladoConsumer;
import com.microservico.customer.application.services.PedidoService;
import org.lib.orderEvents.event.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer implements IPedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-cancelado", groupId = "customer-service")
    @Override
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());
    }
}
