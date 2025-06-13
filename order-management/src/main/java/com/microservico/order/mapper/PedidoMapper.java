package com.microservico.order.mapper;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public static PedidoDtoResponse toDto(Pedido pedido) {
        PedidoDtoResponse pedidoDtoResponse = new PedidoDtoResponse();

        pedidoDtoResponse.setId(pedido.getId());
        pedidoDtoResponse.setClienteId(pedido.getClienteId());
        pedidoDtoResponse.setItens(pedido.getItens().stream().map(ItemPedidoMapper::toDto).toList());
        pedidoDtoResponse.setDataCriacao(pedido.getDataCriacao());
        pedidoDtoResponse.setStatus(pedido.getStatusPedido());
        pedidoDtoResponse.setValorTotal(pedido.getValorTotal());

        return pedidoDtoResponse;
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

        Double vlTotalPedido = itens.stream().mapToDouble(ItemPedido::getValorTotal).sum();

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }
}
