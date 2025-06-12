package com.microservico.customerservice.event;

import com.microservico.customerservice.model.ItemPedido;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PedidoStatusEvent extends PedidoEvent {
    private List<ItemPedidoEvent> itens;
    private Double valorTotal;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPedidoEvent {
        private Long produtoId;
        private String nomeProduto;
        private Integer quantidade;
        private Double precoUnitario;

        public ItemPedidoEvent(ItemPedido itemPedido) {
            this.produtoId = itemPedido.getProdutoId();
            this.nomeProduto = itemPedido.getNomeProduto();
            this.quantidade = itemPedido.getQuantidade();
            this.precoUnitario = itemPedido.getPrecoUnitario();
        }
    }
}
