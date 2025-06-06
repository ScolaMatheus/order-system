package com.microservico.restaurantService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoCanceladoEvent {

    private Long pedidoId;
    private Long restauranteId;
    private Long clienteId;
    private String motivoCancelamento;
    private LocalDateTime dataHoraCancelamento;

}
