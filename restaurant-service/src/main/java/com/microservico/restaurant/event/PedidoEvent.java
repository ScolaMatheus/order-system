package com.microservico.restaurant.event;


import com.microservico.restaurant.util.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoEvent {
    private Long pedidoId;
    private Long restauranteId;
    private Long clienteId;
    private StatusPedido statusPedido;
    private LocalDateTime dataHoraAtualizacao;
}
