package org.lib.orderEvents.event;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PedidoStatusEvent extends PedidoEvent {
    private List<ItemPedidoEvent> itens;
    private BigDecimal valorTotal;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPedidoEvent {
        private Long produtoId;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal precoUnitario;
    }
}
