package com.microservico.customer.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservico.customer.util.StatusPedido;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraAtualizacao;
}
