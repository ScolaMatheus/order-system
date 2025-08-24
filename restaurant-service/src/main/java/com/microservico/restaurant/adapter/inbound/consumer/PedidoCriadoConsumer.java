package com.microservico.restaurant.adapter.inbound.consumer;

import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.application.service.PedidoService;
import com.microservico.restaurant.util.RabbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.RabbitConstants.PEDIDO_CRIADO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CRIADO_QUEUE)
    public void consumirPedido(PedidoStatusEvent event, Message message) {
        int tentativas = getTentativas(message);

        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        log.info("Pedido criado recebido: {}",  event);

        pedidoService.consumirPedido(event);
    }
}
