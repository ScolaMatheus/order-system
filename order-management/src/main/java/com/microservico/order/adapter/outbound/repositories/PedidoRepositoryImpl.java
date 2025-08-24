package com.microservico.order.adapter.outbound.repositories;

import com.microservico.order.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.order.application.repository.PedidoRepository;
import com.microservico.order.model.Pedido;
import com.microservico.order.util.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final JpaPedidoRepository jpaPedidoRepository;

    @Override
    public void save(Pedido pedido) {
        JpaPedidoEntity jpaPedido = PedidoMapper.toJpaEntity(pedido);
        this.jpaPedidoRepository.save(jpaPedido);
    }

    @Override
    public List<Pedido> findAll() {
        return this.jpaPedidoRepository.findAll()
                .stream()
                .map(PedidoMapper::toEntity).toList();
    }

    @Override
    public Optional<Pedido> findById(Long idPedido) {
        Optional<JpaPedidoEntity> pedidoEntity = this.jpaPedidoRepository.findById(idPedido);
        return Optional.of(pedidoEntity.map(PedidoMapper::toEntity)).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        this.jpaPedidoRepository.deleteById(id);
    }
}
