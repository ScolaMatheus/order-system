package com.microservico.customer.application.useCases;

import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import org.lib.orderEvents.event.StatusPedido;
import org.lib.orderEvents.event.PedidoCanceladoEvent;
import org.lib.orderEvents.event.PedidoStatusEvent;

import java.time.LocalDateTime;

public interface PedidoUseCases {

    PedidoDtoResponse criarPedidoEvent(PedidoDtoRequest request);
    PedidoStatusEvent informarPedidoEntregue(Long idPedido);
    void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao);
    PedidoCanceladoEvent processarCancelamentoDePedido(Long idPedido);

}
