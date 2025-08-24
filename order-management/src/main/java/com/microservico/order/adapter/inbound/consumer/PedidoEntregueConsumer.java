package com.microservico.order.adapter.inbound.consumer;

import com.microservico.order.event.PedidoEvent;
import com.microservico.order.util.StatusPedido;
import com.microservico.order.application.service.PedidoService;
import com.microservico.order.util.RabbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.order.util.RabbitConstants.PEDIDO_ENTREGUE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEntregueConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_ENTREGUE_QUEUE)
    public void consumirPedidoEntregue(PedidoEvent event, Message message) {
        int tentativas = getTentativas(message);
        log.info("Pedido entregue recebido: {}",event);

        // Em caso de falha no consumo da mensagem ela será reprocessada 3 vezes, após isso ela será descartada
        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.ENTREGUE, event.getDataHoraAtualizacao());
    }

}
