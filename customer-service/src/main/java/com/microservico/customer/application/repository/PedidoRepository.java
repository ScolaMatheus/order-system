package com.microservico.customer.application.repository;

import com.microservico.customer.model.Pedido;

import java.util.Optional;

public interface PedidoRepository {

    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);

}
