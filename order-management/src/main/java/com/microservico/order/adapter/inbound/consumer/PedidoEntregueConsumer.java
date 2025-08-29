package com.microservico.order.adapter.inbound.consumer;

import com.microservico.order.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEntregueConsumer{

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-entregue", groupId = "order-management")
    public void consumirPedidoEntregue(PedidoEvent event) {
        log.info("Pedido entregue recebido: {}",event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.ENTREGUE, event.getDataHoraAtualizacao());
    }

}
