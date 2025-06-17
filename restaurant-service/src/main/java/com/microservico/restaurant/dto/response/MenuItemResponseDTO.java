package com.microservico.restaurant.dto.response;

import lombok.*;

@Data
public class MenuItemResponseDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Long restaurantId;
    private Boolean ativo;

}
