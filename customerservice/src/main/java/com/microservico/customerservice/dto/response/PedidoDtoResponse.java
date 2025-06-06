package com.microservico.customerservice.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDtoResponse(Long id, Long clienteId, List<ItemPedidoDtoResponse> itens, LocalDateTime dataCriacao,
                                String status, Double valorTotal) implements Serializable {
}
