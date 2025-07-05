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

import static com.microservico.order.util.RabbitConstants.PEDIDO_EM_ROTA_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEmRotaConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_EM_ROTA_QUEUE)
    public void consumirPedidoEmRota(PedidoStatusEvent event, Message message) {
        int tentativas = getTentativas(message);
        log.info("Pedido em rota de entrega recebido: {}", event);

        // Em caso de falha no consumo da mensagem ela será reprocessada 3 vezes, após isso ela será descartada
        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.EM_ROTA, event.getDataHoraAtualizacao());
    }

}
