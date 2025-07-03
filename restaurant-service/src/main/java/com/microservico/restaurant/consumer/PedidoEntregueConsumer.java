package com.microservico.restaurant.consumer;

import com.microservico.restaurant.event.PedidoEvent;
import com.microservico.restaurant.service.PedidoService;
import com.microservico.restaurant.util.RabbitUtil;
import com.microservico.restaurant.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.RabbitConstants.PEDIDO_ENTREGUE_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEntregueConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_ENTREGUE_QUEUE)
    public void consumirPedidoEntregue(PedidoEvent event, Message message) {
        int tentativas = getTentativas(message);

        if (tentativas > 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        log.info("Pedido entregue recebido: {}",event);

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.ENTREGUE, event.getDataHoraAtualizacao());
    }

}
