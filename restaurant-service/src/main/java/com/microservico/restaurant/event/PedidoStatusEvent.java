package com.microservico.restaurant.event;


import com.microservico.restaurant.model.ItemPedido;
import com.microservico.restaurant.model.Pedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PedidoStatusEvent extends PedidoEvent{
    private List<ItemPedidoEvent> itens;
    private BigDecimal valorTotal;

    public PedidoStatusEvent(Pedido pedido) {
        super(pedido.getId(), pedido.getRestauranteId(), pedido.getClienteId(), pedido.getStatusPedido(), LocalDateTime.now());
        this.itens = pedido.getItens().stream().map(ItemPedidoEvent::new).toList();
        this.valorTotal = pedido.getValorTotal();
    }

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
