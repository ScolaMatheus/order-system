package com.microservico.restaurantService.mapper;


import com.microservico.restaurantService.dto.response.ItemPedidoDtoResponse;
import com.microservico.restaurantService.model.ItemPedido;

public class ItemPedidoMapper {

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
