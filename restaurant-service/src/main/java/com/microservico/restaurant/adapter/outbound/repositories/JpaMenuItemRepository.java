package com.microservico.restaurant.adapter.outbound.repositories;

import com.microservico.restaurant.adapter.outbound.entities.JpaMenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMenuItemRepository extends JpaRepository<JpaMenuItemEntity, Long> {
    List<JpaMenuItemEntity> findByRestaurantId(Long restaurantId);
}
