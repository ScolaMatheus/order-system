package com.microservico.restaurant.util.mapper;

import com.microservico.restaurant.adapter.outbound.entities.JpaItemPedidoEntity;
import com.microservico.restaurant.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.model.ItemPedido;
import com.microservico.restaurant.model.Pedido;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PedidoMapper {
    public static Pedido eventToEntity(PedidoStatusEvent event) {
        Pedido pedido = new Pedido();

        pedido.setId(event.getPedidoId());
        pedido.setClienteId(event.getClienteId());
        pedido.setRestauranteId(event.getRestauranteId());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setStatusPedido(event.getStatusPedido());


        // Mapeia os itens
        if (event.getItens() == null || event.getItens().isEmpty()) {
            log.warn("Pedido {} sem itens", event.getPedidoId());
            throw new RecursoNaoEncontradoException("Não foram encontrados itens no Pedido : " + pedido);
        }

        List<ItemPedido> itens = event.getItens().stream().map(itemEvent -> {
            ItemPedido item = new ItemPedido();
            item.setProdutoId(itemEvent.getProdutoId());
            item.setNomeProduto(itemEvent.getNomeProduto());
            item.setQuantidade(itemEvent.getQuantidade());
            item.setPrecoUnitario(itemEvent.getPrecoUnitario());
            item.setPedido(pedido);
            return item;
        }).toList();

        pedido.setItens(itens);
        pedido.setValorTotal(event.getValorTotal());

        return pedido;
    }

    public static PedidoStatusEvent entityToEvent (Pedido pedido) {
        PedidoStatusEvent pedidoEvent = new PedidoStatusEvent();

        pedidoEvent.setPedidoId(pedido.getId());
        pedidoEvent.setRestauranteId(pedido.getRestauranteId());
        pedidoEvent.setClienteId(pedido.getClienteId());
        pedidoEvent.setStatusPedido(pedido.getStatusPedido());
        pedidoEvent.setDataHoraAtualizacao(pedido.getDataAtualizacao());
        pedidoEvent.setItens(pedido.getItens().stream().map(PedidoStatusEvent.ItemPedidoEvent::new).toList());
        pedidoEvent.setValorTotal(pedido.getValorTotal());

        return pedidoEvent;
    }

    public static PedidoDtoResponse toDto(Pedido pedido) {
        return new PedidoDtoResponse(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getRestauranteId(),
                pedido.getItens().stream().map(ItemPedidoMapper::toDto).toList(),
                pedido.getDataCriacao(),
                pedido.getStatusPedido(),
                pedido.getValorTotal()
        );
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
