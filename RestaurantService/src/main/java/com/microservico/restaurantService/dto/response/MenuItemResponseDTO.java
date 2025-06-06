package com.microservico.restaurantService.dto.response;

import lombok.*;

@Data
public class MenuItemResponseDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Long restaurantId;
    private boolean ativo;

}
