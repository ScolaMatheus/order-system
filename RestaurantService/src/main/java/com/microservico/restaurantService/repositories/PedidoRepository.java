package com.microservico.restaurantService.repositories;

import com.microservico.restaurantService.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
