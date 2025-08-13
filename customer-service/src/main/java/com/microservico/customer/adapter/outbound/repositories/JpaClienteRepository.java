package com.microservico.customer.adapter.outbound.repositories;

import com.microservico.customer.adapter.outbound.entities.JpaClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClienteRepository extends JpaRepository<JpaClienteEntity, Long> {

}
