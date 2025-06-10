package com.microservico.restaurantService.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @ToString.Exclude
    private Pedido pedido;

    private Long produtoId;

    private String nomeProduto;

    private Double precoUnitario;

    private Integer quantidade;

    public Double getValorTotal() {
        return this.precoUnitario * this.quantidade;
    }
}
