package com.microservico.restaurant.application.repository;

import com.microservico.restaurant.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository{

    Restaurant save(Restaurant restaurant);
    List<Restaurant> findAll();
    Optional<Restaurant> findById(Long idRestaurant);
    void delete(Restaurant restaurant);

}
