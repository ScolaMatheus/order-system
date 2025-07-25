package com.microservico.order.service;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.exceptions.RecursoNaoEncontradoException;
import com.microservico.order.mapper.PedidoMapper;
import com.microservico.order.model.*;
import com.microservico.order.repositories.PedidoRepository;
import com.microservico.order.util.StatusPedido;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public void criarPedido(PedidoStatusEvent event) {

        Pedido pedido = PedidoMapper.eventToEntity(event);

        pedidoRepository.save(pedido);
    }

    public List<PedidoDtoResponse> buscarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoMapper::toDto)
                .toList();
    }

    public PedidoDtoResponse buscarPedidoPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .map(PedidoMapper::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com id: " + idPedido));
    }

    @Transactional
    public void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO) {
            log.info("Pedido {} não foi alterado pois já está cancelado.", idPedido);
            return;
        }

        pedido.setStatusPedido(statusPedido);
        pedido.setDataAtualizacao(dataHoraAtualizacao);

        pedidoRepository.save(pedido);

        log.info("Pedido {} alterado para status {} às {}",idPedido, statusPedido, dataHoraAtualizacao);
    }

    public void excluirPedido(long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido)
        );
        pedidoRepository.delete(pedido);
    }

}
