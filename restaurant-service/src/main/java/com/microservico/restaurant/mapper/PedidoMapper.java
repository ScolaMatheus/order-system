package com.microservico.restaurant.mapper;

import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.model.ItemPedido;
import com.microservico.restaurant.model.Pedido;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

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
        PedidoDtoResponse dtoResponse = new PedidoDtoResponse();

        dtoResponse.setId(pedido.getId());
        dtoResponse.setClienteId(pedido.getClienteId());
        dtoResponse.setRestaurantId(pedido.getRestauranteId());
        dtoResponse.setItens(pedido.getItens().stream().map(ItemPedidoMapper::toDto).toList());
        dtoResponse.setDataCriacao(pedido.getDataCriacao());
        dtoResponse.setStatus(pedido.getStatusPedido());
        dtoResponse.setValorTotal(pedido.getValorTotal());

        return dtoResponse;
    }
}
