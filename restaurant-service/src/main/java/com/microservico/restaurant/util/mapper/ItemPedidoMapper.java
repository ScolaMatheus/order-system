package com.microservico.restaurant.util.mapper;


import com.microservico.restaurant.adapter.outbound.entities.JpaItemPedidoEntity;
import com.microservico.restaurant.dto.response.ItemPedidoDtoResponse;
import com.microservico.restaurant.model.ItemPedido;
import com.microservico.restaurant.model.Pedido;

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

    public static ItemPedido toEntity(JpaItemPedidoEntity jpaItemPedidoEntity, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setId(jpaItemPedidoEntity.getId());
        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(jpaItemPedidoEntity.getProdutoId());
        itemPedido.setNomeProduto(jpaItemPedidoEntity.getNomeProduto());
        itemPedido.setPrecoUnitario(jpaItemPedidoEntity.getPrecoUnitario());
        itemPedido.setQuantidade(jpaItemPedidoEntity.getQuantidade());

        return itemPedido;
    }
}
