package com.microservico.restaurant.application.repository;

import com.microservico.restaurant.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository{

    void save(Pedido pedido);
    List<Pedido> findByRestauranteId(Long id);
    Optional<Pedido> findById(Long idPedido);

}
