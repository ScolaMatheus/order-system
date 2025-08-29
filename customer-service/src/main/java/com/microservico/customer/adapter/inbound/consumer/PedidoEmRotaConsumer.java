package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoEmRotaConsumer;
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
public class PedidoEmRotaConsumer implements IPedidoEmRotaConsumer {

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-em-rota", groupId = "customer-service")
    @Override
    public void consumirPedidoEmRota(PedidoStatusEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.EM_ROTA, event.getDataHoraAtualizacao());
    }
}
