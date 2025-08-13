package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoCanceladoConsumer;
import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.application.services.PedidoService;
import com.microservico.customer.util.RabbitUtil;
import com.microservico.customer.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.customer.util.OrigemCancelamento.CUSTOMER_SERVICE;
import static com.microservico.customer.util.RabbitConstants.PEDIDO_CANCELADO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer extends RabbitUtil implements IPedidoCanceladoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_CANCELADO_QUEUE)
    public void lidarComMensagemErroRabbitMq(PedidoCanceladoEvent event, Message message) {

        if (event.getOrigemCancelamento().equals(CUSTOMER_SERVICE.toString())) {
            return;
        }

        int tentativas = getTentativas(message);
        log.info("Pedido cancelado recebido: {}", event);

        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        this.consumirPedidoCancelado(event);

    }

    @Override
    public void consumirPedidoCancelado(PedidoCanceladoEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.CANCELADO,event.getDataHoraAtualizacao());
    }

}
