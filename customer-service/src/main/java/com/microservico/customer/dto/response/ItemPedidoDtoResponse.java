package com.microservico.customer.dto.response;

public record ItemPedidoDtoResponse(Long id, Long produtoId, String nomeProduto, Double precoUnitario, int quantidade) {
}
