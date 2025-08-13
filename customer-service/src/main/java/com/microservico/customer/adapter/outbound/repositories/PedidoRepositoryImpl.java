package com.microservico.customer.adapter.outbound.repositories;

import com.microservico.customer.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.customer.application.repository.PedidoRepository;
import com.microservico.customer.model.Pedido;
import com.microservico.customer.util.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final JpaPedidoRepository jpaPedidoRepository;
    @Override
    public Pedido save(Pedido pedido) {
        JpaPedidoEntity pedidoEntity = PedidoMapper.toJpaEntity(pedido);
        this.jpaPedidoRepository.save(pedidoEntity);
        return PedidoMapper.toEntity(pedidoEntity);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        Optional<JpaPedidoEntity> pedidoEntity = this.jpaPedidoRepository.findById(id);
        return Optional.of(pedidoEntity.map(PedidoMapper::toEntity)).orElseThrow();
    }
}
