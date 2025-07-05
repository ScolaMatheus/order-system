package com.microservico.restaurant.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Min(0)
    private BigDecimal preco;

    @NotNull
    private Long restaurantId;

    private Boolean ativo = true;
}