package com.microservico.customer.event;

import com.microservico.customer.model.ItemPedido;
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

        public ItemPedidoEvent(ItemPedido itemPedido) {
            this.produtoId = itemPedido.getProdutoId();
            this.nomeProduto = itemPedido.getNomeProduto();
            this.quantidade = itemPedido.getQuantidade();
            this.precoUnitario = itemPedido.getPrecoUnitario();
        }
    }
}
