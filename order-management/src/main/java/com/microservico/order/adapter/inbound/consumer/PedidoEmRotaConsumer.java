package com.microservico.order.adapter.inbound.consumer;


import com.microservico.order.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.lib.orderEvents.event.PedidoStatusEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEmRotaConsumer{

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-em-rota", groupId = "order-management")
    public void consumirPedidoEmRota(PedidoStatusEvent event) {
        log.info("Pedido em rota de entrega recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.EM_ROTA, event.getDataHoraAtualizacao());
    }

}
