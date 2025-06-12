package com.microservico.restaurantService.consumer;

import com.microservico.restaurantService.event.PedidoEvent;
import com.microservico.restaurantService.service.PedidoService;
import com.microservico.restaurantService.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurantService.util.RabbitConstants.PEDIDO_ENTREGUE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEntregueConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_ENTREGUE_QUEUE)
    public void consumirPedidoEntregue(PedidoEvent event) {
        log.info("Pedido entregue recebido: {}",event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.ENTREGUE, event.getDataHoraAtualizacao());
    }

}
