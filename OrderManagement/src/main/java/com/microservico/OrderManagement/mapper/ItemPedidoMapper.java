package com.microservico.OrderManagement.mapper;

import com.microservico.OrderManagement.dto.request.ItemPedidoDtoRequest;
import com.microservico.OrderManagement.dto.response.ItemPedidoDtoResponse;
import com.microservico.OrderManagement.model.ItemPedido;
import com.microservico.OrderManagement.model.Pedido;

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
        ItemPedidoDtoResponse itensDto = new ItemPedidoDtoResponse();

        itensDto.setId(itemPedido.getId());
        itensDto.setProdutoId(itemPedido.getProdutoId());
        itensDto.setNomeProduto(itemPedido.getNomeProduto());
        itensDto.setPrecoUnitario(itemPedido.getPrecoUnitario());
        itensDto.setQuantidade(itemPedido.getQuantidade());

        return itensDto;

    }
}
