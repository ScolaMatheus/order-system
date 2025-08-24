package com.microservico.restaurant.adapter.outbound.repositories;

import com.microservico.restaurant.adapter.outbound.entities.JpaMenuItemEntity;
import com.microservico.restaurant.application.repository.MenuItemRepository;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.util.mapper.MenuItemMapper;
import com.microservico.restaurant.util.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final JpaMenuItemRepository jpaMenuItemRepository;

    @Override
    public MenuItem save(MenuItem menuItem) {
        JpaMenuItemEntity menuItemEntity = MenuItemMapper.toJpaEntity(menuItem, RestaurantMapper.toJpaEntity(menuItem.getRestaurant()));
        return MenuItemMapper.toEntity(this.jpaMenuItemRepository.save(menuItemEntity), menuItem.getRestaurant());
    }

    @Override
    public Optional<MenuItem> findById(Long idProduto) {
        return this.jpaMenuItemRepository.findById(idProduto)
                .map(menuItemEntity ->
                        MenuItemMapper.toEntity(
                                menuItemEntity,
                                RestaurantMapper.toEntity(menuItemEntity.getRestaurant())
                        ));
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return this.jpaMenuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(menuItemEntity ->
                        MenuItemMapper.toEntity(
                                menuItemEntity,
                                RestaurantMapper.toEntity(menuItemEntity.getRestaurant())
                        )
                ).toList();
    }

    @Override
    public void delete(MenuItem menuItem) {
        this.jpaMenuItemRepository.delete(
                MenuItemMapper.toJpaEntity(
                        menuItem,
                        RestaurantMapper.toJpaEntity(menuItem.getRestaurant())
                )
        );
    }
}
