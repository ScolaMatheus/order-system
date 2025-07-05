package com.microservico.order.consumer;

import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.util.StatusPedido;
import com.microservico.order.service.PedidoService;
import com.microservico.order.util.RabbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.order.util.RabbitConstants.PEDIDO_PREPARANDO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPreparandoConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_PREPARANDO_QUEUE)
    public void consumirPedidoEmPreparo(PedidoStatusEvent event, Message message) {
        log.info("Pedido em preparo recebido: {}", event);
        int tentativas = getTentativas(message);

        // Em caso de falha no consumo da mensagem ela será reprocessada 3 vezes, após isso ela será descartada
        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.PREPARANDO, event.getDataHoraAtualizacao());
    }
}
