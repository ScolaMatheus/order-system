package com.microservico.order.adapter.inbound.consumer;

import com.microservico.order.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer{

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-criado", groupId = "order-management")
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido recebido: {}",  event);
        pedidoService.criarPedido(event);
    }
}
