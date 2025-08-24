package com.microservico.restaurant.adapter.outbound.repositories;

import com.microservico.restaurant.adapter.outbound.entities.JpaPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPedidoRepository extends JpaRepository<JpaPedidoEntity, Long> {

    List<JpaPedidoEntity> findByRestauranteId(Long id);

}
