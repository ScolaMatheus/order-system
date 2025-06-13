package com.microservico.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class PedidoDtoRequest {
    @NotNull
    private Long idCliente;
    @NotNull
    private Long restauranteId;
    @NotEmpty
    private List<ItemPedidoDtoRequest> itens;
}
