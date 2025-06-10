package com.microservico.restaurantService.event;

import com.microservico.restaurantService.util.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PedidoCanceladoEvent extends PedidoEvent{
    private String motivoCancelamento;
    private String origemCancelamento;

    public PedidoCanceladoEvent(
            Long pedidoId,
            Long restauranteId,
            Long clienteId,
            StatusPedido statusPedido,
            LocalDateTime dataHoraAtualizacao,
            String motivoCancelamento,
            String origemCancelamento
    ) {
        super(pedidoId, restauranteId, clienteId, statusPedido, dataHoraAtualizacao);
        this.motivoCancelamento = motivoCancelamento;
        this.origemCancelamento = origemCancelamento;
    }

}
