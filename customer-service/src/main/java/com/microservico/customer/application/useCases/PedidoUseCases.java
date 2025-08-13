package com.microservico.customer.application.useCases;

import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.util.StatusPedido;

import java.time.LocalDateTime;

public interface PedidoUseCases {

    PedidoDtoResponse criarPedidoEvent(PedidoDtoRequest request);
    PedidoStatusEvent informarPedidoEntregue(Long idPedido);
    void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao);
    PedidoCanceladoEvent processarCancelamentoDePedido(Long idPedido);

}
