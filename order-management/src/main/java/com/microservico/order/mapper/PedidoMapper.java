package com.microservico.order.mapper;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
}
