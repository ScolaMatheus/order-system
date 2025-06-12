package com.microservico.customerservice.consumer;

import com.microservico.customerservice.event.PedidoCanceladoEvent;
import com.microservico.customerservice.service.PedidoService;
import com.microservico.customerservice.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.customerservice.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Pedido cancelado recebido: {}", event);
        try {
            pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

        } catch (Exception e) {
            log.error("Erro ao processar evento de pedido [{}]: {}", event.getPedidoId(), e.getMessage(), e);
        }

    }

}
