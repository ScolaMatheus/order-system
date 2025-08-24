package com.microservico.restaurant.application.repository;

import com.microservico.restaurant.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository{

    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(Long idProduto);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    void delete(MenuItem menuItem);

}
