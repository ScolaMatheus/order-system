    package com.microservico.order.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;

    @Entity
    @Table(name = "tb_item_pedido")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ItemPedido {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "pedido_id", nullable = false)
        private Pedido pedido;

        @Column(nullable = false)
        private Long produtoId;

        @Column(nullable = false)
        private String nomeProduto;

        @Column(precision = 10, scale = 2, nullable = false)
        private BigDecimal precoUnitario;

        @Column(nullable = false)
        private Integer quantidade;

        public BigDecimal getValorTotal() {
            if (precoUnitario == null || quantidade == null)
                return BigDecimal.ZERO;
            return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
