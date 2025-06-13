package com.microservico.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PedidoCanceladoEvent extends PedidoEvent{
    private String motivoCancelamento;
    private String origemCancelamento;

}