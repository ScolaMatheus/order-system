package com.microservico.OrderManagement.consumer;

import com.microservico.OrderManagement.event.PedidoStatusEvent;
import com.microservico.OrderManagement.model.StatusPedido;
import com.microservico.OrderManagement.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.PEDIDO_PREPARANDO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPreparandoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_PREPARANDO_QUEUE)
    public void consumirPedidoEmPreparo(PedidoStatusEvent event) {
        log.info("Pedido em preparo recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.PREPARANDO, event.getDataHoraAtualizacao());
    }
}
