package com.microservico.restaurantService.consumer;

import com.microservico.restaurantService.event.PedidoCanceladoEvent;
import com.microservico.restaurantService.service.PedidoService;
import com.microservico.restaurantService.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurantService.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Pedido cancelado recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

    }

}
