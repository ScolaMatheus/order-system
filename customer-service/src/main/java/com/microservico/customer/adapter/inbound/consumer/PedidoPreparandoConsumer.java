package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoPreparandoConsumer;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.application.services.PedidoService;
import com.microservico.customer.util.RabbitUtil;
import com.microservico.customer.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.customer.util.RabbitConstants.PEDIDO_PREPARANDO_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPreparandoConsumer extends RabbitUtil implements IPedidoPreparandoConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_PREPARANDO_QUEUE)
    public void lidarComMensagemErroRabbitMq(PedidoStatusEvent event, Message message) {
        int tentativas = getTentativas(message);
        log.info("Pedido em preparo recebido: {}", event);

        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }
        this.consumirPedidoEmPreparo(event);
    }

    @Override
    public void consumirPedidoEmPreparo(PedidoStatusEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.PREPARANDO, event.getDataHoraAtualizacao());
    }
}
