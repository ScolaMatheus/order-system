package com.microservico.order.dto.response;

import java.math.BigDecimal;

public record ItemPedidoDtoResponse (Long id, Long produtoId, String nomeProduto, BigDecimal precoUnitario, int quantidade
) {}
