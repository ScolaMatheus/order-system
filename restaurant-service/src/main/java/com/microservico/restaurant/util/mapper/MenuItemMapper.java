package com.microservico.restaurant.util.mapper;

import com.microservico.restaurant.adapter.outbound.entities.JpaMenuItemEntity;
import com.microservico.restaurant.adapter.outbound.entities.JpaRestaurantEntity;
import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Restaurant;

public class MenuItemMapper {

    public static MenuItem toEntity(MenuItemRequestDTO dto, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();

        menuItem.setNome(dto.getNome());
        menuItem.setPreco(dto.getPreco());
        menuItem.setRestaurant(restaurant);
        menuItem.setAtivo(dto.getAtivo());

        return menuItem;
    }

    public static MenuItemResponseDTO toDto(MenuItem menuItem) {
        return new MenuItemResponseDTO(
                menuItem.getId(),
                menuItem.getNome(),
                menuItem.getPreco(),
                menuItem.getRestaurant().getId(),
                menuItem.getAtivo()
        );
    }

    public static MenuItem toEntity(JpaMenuItemEntity menuItemEntity, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();

        menuItem.setNome(menuItemEntity.getNome());
        menuItem.setPreco(menuItemEntity.getPreco());
        menuItem.setRestaurant(restaurant);
        menuItem.setAtivo(menuItemEntity.getAtivo());

        return menuItem;
    }

    public static JpaMenuItemEntity toJpaEntity(MenuItem menuItem, JpaRestaurantEntity restaurant) {

        JpaMenuItemEntity jpaMenuItemEntity = new JpaMenuItemEntity();

        jpaMenuItemEntity.setNome(menuItem.getNome());
        jpaMenuItemEntity.setPreco(menuItem.getPreco());
        jpaMenuItemEntity.setRestaurant(restaurant);
        jpaMenuItemEntity.setAtivo(menuItem.getAtivo());

        return jpaMenuItemEntity;

    }
}
