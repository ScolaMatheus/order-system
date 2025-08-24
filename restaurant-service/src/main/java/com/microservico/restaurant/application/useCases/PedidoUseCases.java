package com.microservico.restaurant.application.useCases;

import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.util.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoUseCases {

    void consumirPedido(PedidoStatusEvent event);
    PedidoStatusEvent informarPedidoPronto(Long idPedido);
    List<PedidoDtoResponse> buscarPedidoPorRestaurante(Long idRestaurant);
    void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao);


}
