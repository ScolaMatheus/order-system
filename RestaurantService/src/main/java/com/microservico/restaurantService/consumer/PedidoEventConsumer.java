package com.microservico.restaurantService.consumer;

import com.microservico.restaurantService.event.PedidoCanceladoEvent;
import com.microservico.restaurantService.event.PedidoCriadoEvent;
import com.microservico.restaurantService.publisher.PedidoEventPublisher;
import com.microservico.restaurantService.service.PedidoValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoEventConsumer {

    private final PedidoValidationService pedidoValidationService;
    private final PedidoEventPublisher pedidoEventPublisher;

    @RabbitListener(queues = "pedido.criado.queue")
    public void consumirPedido(PedidoCriadoEvent event) {
        log.info("Pedido recebido: {}",  event);

        boolean pedidoValido = pedidoValidationService.validarRestautanteEItens(event.getRestauranteId(), event.getItens());

        if (pedidoValido) {
            log.info("Pedido {} aceito pelo restaurante {}", event.getPedidoId(), event.getRestauranteId());
        } else {
            log.warn("Pedido {} CANCELADO - restaurante ou item inválido/inativado", event.getPedidoId());
//            pedidoEventPublisher.publicarPedidoCancelado(new PedidoCanceladoEvent(
//                    event.getPedidoId(),
//                    event.getRestauranteId(),
//                    event.getClienteId(),
//                    "Pedido cancelado devido a Restaurante ou item inválido/inativado",
//                    LocalDateTime.now()
//            ));
        }
    }
}
