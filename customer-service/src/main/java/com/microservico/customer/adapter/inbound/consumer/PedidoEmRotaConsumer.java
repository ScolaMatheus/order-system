package com.microservico.customer.adapter.inbound.consumer;

import com.microservico.customer.application.consumer.IPedidoEmRotaConsumer;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.application.services.PedidoService;
import com.microservico.customer.util.RabbitUtil;
import com.microservico.customer.util.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.microservico.customer.util.RabbitConstants.PEDIDO_EM_ROTA_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEmRotaConsumer extends RabbitUtil implements IPedidoEmRotaConsumer {

    private final PedidoService pedidoService;

    @RabbitListener(queues = PEDIDO_EM_ROTA_QUEUE)
    public void lidarComMensagemErroRabbitMq(PedidoStatusEvent event, Message message) {
        int tentativas = getTentativas(message);
        log.info("Pedido em rota de entrega recebido: {}", event);

        if (tentativas >= 3) {
            log.warn("Mensagem ignorada após {} tentativas: {}", tentativas, event);
            return;
        }

        this.consumirPedidoEmRota(event);

    }

    @Override
    public void consumirPedidoEmRota(PedidoStatusEvent event) {
        pedidoService.atualizarPedido(event.getPedidoId(), StatusPedido.EM_ROTA, event.getDataHoraAtualizacao());

    }
}
