package com.microservico.order.dto.response;

import com.microservico.order.util.StatusPedido;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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

    private StatusPedido status;

    private BigDecimal valorTotal;

}
