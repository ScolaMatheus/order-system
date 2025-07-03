package com.microservico.restaurant.consumer;

import com.microservico.restaurant.event.PedidoCanceladoEvent;
import com.microservico.restaurant.service.PedidoService;
import com.microservico.restaurant.util.RabbitUtil;
import com.microservico.restaurant.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.restaurant.util.OrigemCancelamento.CUSTOMER_SERVICE;
import static com.microservico.restaurant.util.OrigemCancelamento.RESTAURANT_SERVICE;
import static com.microservico.restaurant.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer extends RabbitUtil {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void consumirPedidoCancelado(PedidoCanceladoEvent event, Message message) {

        if (event.getOrigemCancelamento().equals(RESTAURANT_SERVICE.name())) {
            return;
        }

        int tentativas = getTentativas(message);

        if (tentativas > 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        log.info("Pedido cancelado recebido: {}", event);

        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());

    }

}
