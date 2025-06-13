package com.microservico.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    private boolean ativo = true;

}
