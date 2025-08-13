package com.microservico.customer.adapter.outbound.repositories;

import com.microservico.customer.adapter.outbound.entities.JpaPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPedidoRepository extends JpaRepository<JpaPedidoEntity, Long> {
}
