package com.microservico.restaurant.dto.response;

import lombok.Data;

@Data
public class RestaurantResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
    private boolean ativo;
}
