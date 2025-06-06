package com.microservico.restaurantService.event;


import com.microservico.restaurantService.model.ItemPedido;
import com.microservico.restaurantService.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoCriadoEvent {

    private Long pedidoId;
    private Long restauranteId;
    private Long clienteId;
    private List<ItemPedidoEvent> itens;
    private Double valorTotal;

    public PedidoCriadoEvent(Pedido pedido) {
        this.pedidoId = pedido.getId();
        this.restauranteId = pedido.getRestauranteId();
        this.clienteId = pedido.getClienteId();
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
        private Double precoUnitario;

        public ItemPedidoEvent(ItemPedido itemPedido) {
            this.produtoId = itemPedido.getProdutoId();
            this.nomeProduto = itemPedido.getNomeProduto();
            this.quantidade = itemPedido.getQuantidade();
            this.precoUnitario = itemPedido.getPrecoUnitario();
        }
    }
}
