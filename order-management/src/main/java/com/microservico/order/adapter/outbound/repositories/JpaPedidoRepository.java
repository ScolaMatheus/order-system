package com.microservico.order.adapter.outbound.repositories;

import com.microservico.order.adapter.outbound.entities.JpaPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPedidoRepository extends JpaRepository<JpaPedidoEntity, Long> {
}
