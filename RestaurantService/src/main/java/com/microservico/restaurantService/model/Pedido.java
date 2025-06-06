package com.microservico.restaurantService.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {


    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private List<ItemPedido> itens;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private StatusPedido statusPedido;
    private Double valorTotal;

    public Double getValorTotal() {
        if (itens == null) return 0.0;
        return itens.stream()
                .mapToDouble(ItemPedido::getValorTotal)
                .sum();
    }
}
