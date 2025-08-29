package com.microservico.restaurant.adapter.inbound.consumer;

import com.microservico.restaurant.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.lib.orderEvents.event.StatusPedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.OrigemCancelamento.RESTAURANT_SERVICE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @KafkaListener(topics = "pedido-cancelado", groupId = "restaurant-service")
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {

        if (event.getOrigemCancelamento().equals(RESTAURANT_SERVICE.name())) {
            return;
        }

        log.info("Pedido cancelado recebido: {}", event);

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

    }
}
