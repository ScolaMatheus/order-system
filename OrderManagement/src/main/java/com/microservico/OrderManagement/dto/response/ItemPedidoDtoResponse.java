package com.microservico.OrderManagement.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemPedidoDtoResponse {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Double precoUnitario;
    private int quantidade;
}
