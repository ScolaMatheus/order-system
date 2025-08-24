package com.microservico.restaurant.adapter.outbound.repositories;

import com.microservico.restaurant.adapter.outbound.entities.JpaPedidoEntity;
import com.microservico.restaurant.application.repository.PedidoRepository;
import com.microservico.restaurant.model.Pedido;
import com.microservico.restaurant.util.mapper.PedidoMapper;
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
        JpaPedidoEntity pedidoEntity = PedidoMapper.toJpaEntity(pedido);
        this.jpaPedidoRepository.save(pedidoEntity);
    }

    @Override
    public List<Pedido> findByRestauranteId(Long id) {
        return this.jpaPedidoRepository.findByRestauranteId(id)
                .stream()
                .map(PedidoMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<Pedido> findById(Long idPedido) {
        Optional<JpaPedidoEntity> jpaPedidoEntity = this.jpaPedidoRepository.findById(idPedido);
        return Optional.of(jpaPedidoEntity.map(PedidoMapper::toEntity)).orElseThrow();
    }
}
