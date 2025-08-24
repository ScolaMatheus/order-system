package com.microservico.order.application.repositories;

import com.microservico.order.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {


    void save(Pedido pedido);
    List<Pedido> findAll();
    Optional<Pedido> findById(Long idPedido);
    void delete(Long id);
}
