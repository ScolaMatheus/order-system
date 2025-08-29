package com.microservico.order.adapter.inbound.consumer;

import com.microservico.order.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer{

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-cancelado", groupId = "order-management")
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Pedido cancelado recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

    }

}
