package com.microservico.restaurantService.repositories;

import com.microservico.restaurantService.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByRestauranteId(Long id);
}
