package com.microservico.restaurant.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Min(0)
    private Double preco;

    @NotNull
    private Long restaurantId;

    private Boolean ativo = true;
}