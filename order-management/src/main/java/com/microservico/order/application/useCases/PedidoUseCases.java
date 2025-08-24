package com.microservico.order.application.useCases;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.util.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoUseCases {

    void criarPedido(PedidoStatusEvent event);

    List<PedidoDtoResponse> buscarPedidos();

    PedidoDtoResponse buscarPedidoPorId(Long idPedido);

    void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao);

    void excluirPedido(Long idPedido);
}
