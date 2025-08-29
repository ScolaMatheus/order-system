package com.microservico.restaurant.adapter.inbound.consumer;

import com.microservico.restaurant.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer {

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-criado", groupId = "restaurant-service")
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido criado recebido: {}",  event);
        pedidoService.consumirPedido(event);
    }
}
