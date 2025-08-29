package com.microservico.order.application.useCases;

import com.microservico.order.dto.response.PedidoDtoResponse;
import org.lib.orderEvents.event.PedidoStatusEvent;
import org.lib.orderEvents.event.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoUseCases {

    void criarPedido(PedidoStatusEvent event);

    List<PedidoDtoResponse> buscarPedidos();

    PedidoDtoResponse buscarPedidoPorId(Long idPedido);

    void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao);

    void excluirPedido(Long idPedido);
}
