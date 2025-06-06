package com.microservico.customerservice.dto.response;

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

    private List<ItemPedidoDtoResponse> itens;

    private LocalDateTime dataCriacao;

    private String status;

    private Double valorTotal;

}
