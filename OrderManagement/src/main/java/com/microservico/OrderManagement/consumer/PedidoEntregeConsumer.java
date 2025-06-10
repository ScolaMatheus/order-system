package com.microservico.OrderManagement.consumer;

import com.microservico.OrderManagement.event.PedidoEvent;
import com.microservico.OrderManagement.model.StatusPedido;
import com.microservico.OrderManagement.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.PEDIDO_ENTREGE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEntregeConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_ENTREGE_QUEUE)
    public void consumirPedidoEntrege(PedidoEvent event) {
        log.info("Pedido entrege recebido: {}",event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.ENTREGUE, event.getDataHoraAtualizacao());
    }

}
