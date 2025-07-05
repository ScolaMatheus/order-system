package com.microservico.restaurant.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
public class MenuItemResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Long restaurantId;
    private Boolean ativo;

}
