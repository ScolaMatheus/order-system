package com.microservico.customerservice.mapper;

import com.microservico.customerservice.dto.request.PedidoDtoRequest;
import com.microservico.customerservice.dto.response.PedidoDtoResponse;
import com.microservico.customerservice.event.PedidoStatusEvent;
import com.microservico.customerservice.exceptions.RecursoNaoEncontradoException;
import com.microservico.customerservice.model.ItemPedido;
import com.microservico.customerservice.model.Pedido;
import com.microservico.customerservice.util.StatusPedido;
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

        Double vlTotalPedido = itens.stream().mapToDouble(ItemPedido::getValorTotal).sum();

        pedido.setValorTotal(vlTotalPedido);

        return pedido;
    }
}
