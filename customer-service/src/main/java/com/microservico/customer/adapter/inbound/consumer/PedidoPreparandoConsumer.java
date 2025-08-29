package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoPreparandoConsumer;
import com.microservico.customer.application.services.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoStatusEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPreparandoConsumer implements IPedidoPreparandoConsumer {

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-preparando", groupId = "customer-service")
    @Override
    public void consumirPedidoEmPreparo(PedidoStatusEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.PREPARANDO, event.getDataHoraAtualizacao());
    }
}