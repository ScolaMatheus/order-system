package com.microservico.OrderManagement.publisher;


import com.microservico.OrderManagement.config.RabbitMQConfig;
import com.microservico.OrderManagement.event.PedidoCriadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCriado(PedidoCriadoEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PEDIDO_EXCHANGE,
                RabbitMQConfig.PEDIDO_ROUTING_KEY,
                event
        );

        log.info("Evento PedidoCriado enviado para exchange={} com routingKey={}",
                RabbitMQConfig.PEDIDO_EXCHANGE,
                RabbitMQConfig.PEDIDO_ROUTING_KEY);
    }

}
