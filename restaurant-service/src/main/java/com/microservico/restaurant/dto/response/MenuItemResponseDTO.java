package com.microservico.restaurant.dto.response;

import java.math.BigDecimal;

public record MenuItemResponseDTO (Long id, String nome, BigDecimal preco, Long restaurantId, Boolean ativo) {
}
