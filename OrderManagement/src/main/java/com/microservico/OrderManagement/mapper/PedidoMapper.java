package com.microservico.OrderManagement.mapper;

import com.microservico.OrderManagement.dto.request.PedidoDtoRequest;
import com.microservico.OrderManagement.dto.response.PedidoDtoResponse;
import com.microservico.OrderManagement.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public Pedido toEntity(PedidoDtoRequest dto) {
        Pedido pedido = new Pedido();

        pedido.setClienteId(dto.getIdCliente());
        pedido.setRestauranteId(dto.getRestauranteId());
        pedido.setStatusPedido(StatusPedido.CRIADO);

        //Associa os itens ao pedido
        List<ItemPedido> itens = dto.getItens().stream()
                .map(itemDto -> ItemPedidoMapper.toEntity(itemDto, pedido))
                .toList();

        pedido.setItens(itens);

        Double vlTotalPedido = itens.stream().mapToDouble(ItemPedido::getValorTotal).sum();

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }

    public PedidoDtoResponse toDto(Pedido pedido) {
        PedidoDtoResponse pedidoDtoResponse = new PedidoDtoResponse();

        pedidoDtoResponse.setId(pedido.getId());
        pedidoDtoResponse.setClienteId(pedido.getClienteId());
        pedidoDtoResponse.setItens(pedido.getItens().stream().map(ItemPedidoMapper::toDto).toList());
        pedidoDtoResponse.setDataCriacao(pedido.getDataCriacao());
        pedidoDtoResponse.setStatus(pedido.getStatusPedido());
        pedidoDtoResponse.setValorTotal(pedido.getValorTotal());

        return pedidoDtoResponse;
    }
}
