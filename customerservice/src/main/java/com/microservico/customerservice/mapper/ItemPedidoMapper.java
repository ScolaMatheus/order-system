package com.microservico.customerservice.mapper;

import com.microservico.customerservice.dto.response.ItemPedidoDtoResponse;
import com.microservico.customerservice.model.ItemPedido;

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
