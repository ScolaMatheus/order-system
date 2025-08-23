package com.microservico.order.util.mapper;

import com.microservico.order.adapter.outbound.entities.JpaItemPedidoEntity;
import com.microservico.order.dto.response.ItemPedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.ItemPedido;
import com.microservico.order.model.Pedido;

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

    public static ItemPedido eventToEntity(PedidoStatusEvent.ItemPedidoEvent itemPedidoEvent, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(itemPedidoEvent.getProdutoId());
        itemPedido.setNomeProduto(itemPedidoEvent.getNomeProduto());
        itemPedido.setPrecoUnitario(itemPedidoEvent.getPrecoUnitario());
        itemPedido.setQuantidade(itemPedidoEvent.getQuantidade());

        return itemPedido;
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
