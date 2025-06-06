package com.microservico.OrderManagement.service;

import com.microservico.OrderManagement.dto.request.PedidoDtoRequest;
import com.microservico.OrderManagement.dto.response.PedidoDtoResponse;
import com.microservico.OrderManagement.event.PedidoCriadoEvent;
import com.microservico.OrderManagement.exceptions.RecursoNaoEncontradoException;
import com.microservico.OrderManagement.mapper.PedidoMapper;
import com.microservico.OrderManagement.model.*;
import com.microservico.OrderManagement.publisher.PedidoEventPublisher;
import com.microservico.OrderManagement.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoEventPublisher pedidoEventPublisher;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoDtoResponse criarPedido(PedidoDtoRequest dto) {

        Pedido pedido = pedidoMapper.toEntity(dto);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        PedidoCriadoEvent event = new PedidoCriadoEvent(pedidoSalvo);

        pedidoEventPublisher.publicarPedidoCriado(event);

        return pedidoMapper.toDto(pedidoSalvo);
    }

    public List<PedidoDtoResponse> buscarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toDto)
                .toList();
    }

    public PedidoDtoResponse buscarPedidoPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .map(pedidoMapper::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com id: " + idPedido));
    }

    public void excluirPedido(long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido)
        );
        pedidoRepository.delete(pedido);
    }

}
