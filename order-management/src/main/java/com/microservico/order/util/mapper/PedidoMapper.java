package com.microservico.order.util.mapper;

import com.microservico.order.adapter.outbound.entities.JpaItemPedidoEntity;
import com.microservico.order.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public static PedidoDtoResponse toDto(Pedido pedido) {
        return new PedidoDtoResponse(
        pedido.getId(),
        pedido.getClienteId(),
        pedido.getItens().stream().map(ItemPedidoMapper::toDto).toList(),
        pedido.getDataCriacao(),
        pedido.getDataAtualizacao(),
        pedido.getStatusPedido(),
        pedido.getValorTotal()
        );
    }

    public static Pedido eventToEntity(PedidoStatusEvent event) {
        Pedido pedido = new Pedido();

        pedido.setId(event.getPedidoId());
        pedido.setClienteId(event.getClienteId());
        pedido.setRestauranteId(event.getRestauranteId());
        pedido.setStatusPedido(event.getStatusPedido());

        //Associa os itens ao pedido
        List<ItemPedido> itens = event.getItens().stream()
                .map(itemEvent -> ItemPedidoMapper.eventToEntity(itemEvent, pedido))
                .toList();

        pedido.setItens(itens);

        BigDecimal vlTotalPedido = itens.stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }

    public static Pedido toEntity(JpaPedidoEntity pedidoEntity) {
        Pedido pedido = new Pedido();

        pedido.setId(pedidoEntity.getId());
        pedido.setClienteId(pedidoEntity.getClienteId());
        pedido.setRestauranteId(pedidoEntity.getRestauranteId());
        pedido.setStatusPedido(pedidoEntity.getStatusPedido());
        pedido.setDataCriacao(pedidoEntity.getDataCriacao());
        pedido.setDataAtualizacao(pedidoEntity.getDataAtualizacao());

        List<ItemPedido> itens = pedidoEntity.getItens()
                .stream()
                .map(itemEntity -> ItemPedidoMapper.toEntity(itemEntity, pedido)).toList();

        pedido.setItens(itens);

        BigDecimal vlTotalPedido = itens.stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }

    public static JpaPedidoEntity toJpaEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        JpaPedidoEntity jpaPedido = new JpaPedidoEntity();
        jpaPedido.setId(pedido.getId());
        jpaPedido.setClienteId(pedido.getClienteId());
        jpaPedido.setRestauranteId(pedido.getRestauranteId());
        jpaPedido.setDataCriacao(pedido.getDataCriacao());
        jpaPedido.setDataAtualizacao(pedido.getDataAtualizacao());
        jpaPedido.setStatusPedido(pedido.getStatusPedido());
        jpaPedido.setValorTotal(pedido.getValorTotal());

        List<JpaItemPedidoEntity> jpaItens = pedido.getItens().stream()
                .map(item -> toJpaItemPedidoEntity(item, jpaPedido))
                .collect(Collectors.toList());
        jpaPedido.setItens(jpaItens);

        return jpaPedido;
    }

    private static JpaItemPedidoEntity toJpaItemPedidoEntity(ItemPedido item, JpaPedidoEntity jpaPedido) {
        if (item == null) {
            return null;
        }

        JpaItemPedidoEntity jpaItem = new JpaItemPedidoEntity();
        jpaItem.setId(item.getId());
        jpaItem.setProdutoId(item.getProdutoId());
        jpaItem.setNomeProduto(item.getNomeProduto());
        jpaItem.setPrecoUnitario(item.getPrecoUnitario());
        jpaItem.setQuantidade(item.getQuantidade());
        jpaItem.setPedido(jpaPedido);

        return jpaItem;
    }
}
