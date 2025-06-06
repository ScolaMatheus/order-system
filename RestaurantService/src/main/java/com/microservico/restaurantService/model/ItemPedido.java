package com.microservico.restaurantService.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemPedido {

    private long id;
    private Pedido pedido;
    private Long produtoId;
    private String nomeProduto;
    private Double precoUnitario;
    private Integer quantidade;

    public Double getValorTotal() {
        return this.precoUnitario * this.quantidade;
    }
}
