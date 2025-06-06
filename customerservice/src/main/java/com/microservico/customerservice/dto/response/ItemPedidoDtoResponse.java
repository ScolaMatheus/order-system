package com.microservico.customerservice.dto.response;

public record ItemPedidoDtoResponse(Long id, Long produtoId, String nomeProduto, Double precoUnitario, int quantidade) {
}
