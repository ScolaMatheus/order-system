package com.microservico.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    private boolean ativo = true;

}
