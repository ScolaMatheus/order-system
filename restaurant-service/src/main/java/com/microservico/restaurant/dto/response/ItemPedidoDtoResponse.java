package com.microservico.restaurant.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemPedidoDtoResponse {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private int quantidade;
}
