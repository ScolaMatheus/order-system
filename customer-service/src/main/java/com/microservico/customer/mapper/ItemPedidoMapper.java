package com.microservico.customer.mapper;

import com.microservico.customer.dto.request.ItemPedidoDtoRequest;
import com.microservico.customer.dto.response.ItemPedidoDtoResponse;
import com.microservico.customer.model.ItemPedido;
import com.microservico.customer.model.Pedido;

public class ItemPedidoMapper {

    public static ItemPedidoDtoResponse toDto(ItemPedido itemPedido) {
        return new ItemPedidoDtoResponse(
                itemPedido.getId(),
                itemPedido.getProdutoId(),
                itemPedido.getNomeProduto(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getQuantidade()
        );
    }

    public static ItemPedido toEntity(ItemPedidoDtoRequest dtoRequest, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(dtoRequest.getProdutoId());
        itemPedido.setNomeProduto(dtoRequest.getNomeProduto());
        itemPedido.setPrecoUnitario(dtoRequest.getPrecoUnitario());
        itemPedido.setQuantidade(dtoRequest.getQuantidade());

        return itemPedido;
    }
}
