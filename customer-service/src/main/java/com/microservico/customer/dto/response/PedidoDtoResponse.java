package com.microservico.customer.dto.response;

import com.microservico.customer.util.StatusPedido;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDtoResponse(
        Long id, Long clienteId, Long restauranteId, List<ItemPedidoDtoResponse> itens, LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao, StatusPedido status, Double valorTotal
) implements Serializable {
}
