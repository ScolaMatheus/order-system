package com.microservico.restaurantService.model;

import com.microservico.restaurantService.util.StatusPedido;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pedido")
public class Pedido {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private Long restauranteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens;

    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private StatusPedido statusPedido;

    @Column(nullable = false)
    private Double valorTotal;

    public Double getValorTotal() {
        if (itens == null) return 0.0;
        return itens.stream()
                .mapToDouble(ItemPedido::getValorTotal)
                .sum();
    }
}
