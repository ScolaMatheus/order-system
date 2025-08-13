package com.microservico.customer.util.mapper;

import com.microservico.customer.adapter.outbound.entities.JpaItemPedidoEntity;
import com.microservico.customer.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.model.ItemPedido;
import com.microservico.customer.model.Pedido;
import com.microservico.customer.util.StatusPedido;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PedidoMapper {

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
                pedido.getDataAtualizacao(),
                pedido.getStatusPedido(),
                pedido.getValorTotal()
        );
    }

    public static Pedido toEntity(PedidoDtoRequest request) {
        Pedido pedido = new Pedido();

        pedido.setClienteId(request.getIdCliente());
        pedido.setRestauranteId(request.getRestauranteId());
        pedido.setStatusPedido(StatusPedido.CRIADO);

        //Associa os itens ao pedido
        List<ItemPedido> itens = request.getItens().stream()
                .map(itemDto -> ItemPedidoMapper.toEntity(itemDto, pedido))
                .toList();

        pedido.setItens(itens);

        BigDecimal vlTotalPedido = itens.stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }

    public static Pedido toEntity(JpaPedidoEntity pedidoEntity) {
        Pedido pedido = new Pedido();

        pedido.setClienteId(pedidoEntity.getClienteId());
        pedido.setRestauranteId(pedidoEntity.getRestauranteId());
        pedido.setStatusPedido(StatusPedido.CRIADO);

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
        jpaItem.setPedido(jpaPedido); // <<-- AQUI ESTÁ A CORREÇÃO CRÍTICA

        return jpaItem;
    }
}
