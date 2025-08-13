package com.microservico.customer.adapter.outbound.entities;

import com.microservico.customer.model.Pedido;
import com.microservico.customer.util.StatusPedido;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pedido")
public class JpaPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private Long restauranteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JpaItemPedidoEntity> itens;

    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private StatusPedido statusPedido;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    public BigDecimal getValorTotal() {
        if (itens == null) return new BigDecimal(0);
        return itens.stream()
                .map(JpaItemPedidoEntity::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public JpaPedidoEntity(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getClienteId();
        this.restauranteId = pedido.getRestauranteId();
        this.itens = pedido.getItens().stream().map(itemPedido -> new JpaItemPedidoEntity(itemPedido.getId(), new JpaPedidoEntity(itemPedido.getPedido()), itemPedido.getProdutoId(), itemPedido.getNomeProduto(), itemPedido.getPrecoUnitario(), itemPedido.getQuantidade())).toList();
        this.dataCriacao = pedido.getDataCriacao();
        this.dataAtualizacao = pedido.getDataAtualizacao();
        this.statusPedido = pedido.getStatusPedido();
        this.valorTotal = pedido.getValorTotal();
    }

}
