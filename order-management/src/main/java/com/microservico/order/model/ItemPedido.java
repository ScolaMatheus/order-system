    package com.microservico.order.model;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "tb_item_pedido")
    @Data
    @NoArgsConstructor
    public class ItemPedido {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @ManyToOne
        @JoinColumn(name = "pedido_id", nullable = false)
        private Pedido pedido;

        @Column(nullable = false)
        private Long produtoId;

        @Column(nullable = false)
        private String nomeProduto;

        @Column(nullable = false)
        private Double precoUnitario;

        @Column(nullable = false)
        private Integer quantidade;

        public Double getValorTotal() {
            return this.precoUnitario * this.quantidade;
        }
    }
