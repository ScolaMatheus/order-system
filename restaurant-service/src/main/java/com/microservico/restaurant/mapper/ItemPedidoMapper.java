package com.microservico.restaurant.mapper;


import com.microservico.restaurant.dto.response.ItemPedidoDtoResponse;
import com.microservico.restaurant.model.ItemPedido;

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
}
