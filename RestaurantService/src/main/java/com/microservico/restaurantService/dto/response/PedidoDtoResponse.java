package com.microservico.restaurantService.dto.response;


import com.microservico.restaurantService.util.StatusPedido;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PedidoDtoResponse implements Serializable {
    private Long id;

    private Long clienteId;

    private Long restaurantId;

    private List<ItemPedidoDtoResponse> itens;

    private LocalDateTime dataCriacao;

    private StatusPedido status;

    private Double valorTotal;

}
