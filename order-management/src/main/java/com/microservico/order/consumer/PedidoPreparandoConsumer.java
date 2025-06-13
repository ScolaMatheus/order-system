package com.microservico.order.consumer;

import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.StatusPedido;
import com.microservico.order.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.order.util.RabbitConstants.PEDIDO_PREPARANDO_QUEUE;

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
