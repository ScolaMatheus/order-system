package com.microservico.customer.adapter.outbound.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_item_pedido")
public class JpaItemPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private JpaPedidoEntity pedido;

    @Column(nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    public BigDecimal getValorTotal() {
        if (precoUnitario == null || quantidade == null) return BigDecimal.ZERO;
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

}
