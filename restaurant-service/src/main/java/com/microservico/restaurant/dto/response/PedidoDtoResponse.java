package com.microservico.restaurant.dto.response;


import com.microservico.restaurant.util.StatusPedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDtoResponse (
        Long id, Long clienteId, Long restaurantId, List<ItemPedidoDtoResponse> itens, LocalDateTime dataCriacao,
        StatusPedido statusPedido, BigDecimal valorTotal
) implements Serializable {
}
