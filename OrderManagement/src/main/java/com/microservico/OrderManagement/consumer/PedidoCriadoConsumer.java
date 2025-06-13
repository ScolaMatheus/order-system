package com.microservico.OrderManagement.consumer;

import com.microservico.OrderManagement.event.PedidoStatusEvent;
import com.microservico.OrderManagement.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.PEDIDO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_QUEUE)
    public void consumirPedido(PedidoStatusEvent event) {
        log.info("Pedido recebido: {}",  event);
        pedidoService.criarPedido(event);
    }
}
