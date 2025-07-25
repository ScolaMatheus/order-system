package com.microservico.order.mapper;

import com.microservico.order.dto.request.ItemPedidoDtoRequest;
import com.microservico.order.dto.response.ItemPedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.ItemPedido;
import com.microservico.order.model.Pedido;

public class ItemPedidoMapper {

    public static ItemPedido toEntity(ItemPedidoDtoRequest dtoRequest, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(dtoRequest.getProdutoId());
        itemPedido.setNomeProduto(dtoRequest.getNomeProduto());
        itemPedido.setPrecoUnitario(dtoRequest.getPrecoUnitario());
        itemPedido.setQuantidade(dtoRequest.getQuantidade());

        return itemPedido;
    }

    public static ItemPedidoDtoResponse toDto(ItemPedido itemPedido) {
        return new ItemPedidoDtoResponse(
                itemPedido.getId(),
                itemPedido.getProdutoId(),
                itemPedido.getNomeProduto(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getQuantidade()
        );
    }

    public static ItemPedido eventToEntity(PedidoStatusEvent.ItemPedidoEvent itemPedidoEvent, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(itemPedidoEvent.getProdutoId());
        itemPedido.setNomeProduto(itemPedidoEvent.getNomeProduto());
        itemPedido.setPrecoUnitario(itemPedidoEvent.getPrecoUnitario());
        itemPedido.setQuantidade(itemPedidoEvent.getQuantidade());

        return itemPedido;
    }
}
