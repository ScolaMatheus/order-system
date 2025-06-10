package com.microservico.OrderManagement.consumer;

import com.microservico.OrderManagement.event.PedidoStatusEvent;
import com.microservico.OrderManagement.model.StatusPedido;
import com.microservico.OrderManagement.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.OrderManagement.util.RabbitConstants.PEDIDO_EM_ROTA_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEmRotaConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_EM_ROTA_QUEUE)
    public void consumirPedidoEmRota(PedidoStatusEvent event) {
        log.info("Pedido em rota de entrega recebido: {}", event);
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.EM_ROTA, event.getDataHoraAtualizacao());
    }

}
