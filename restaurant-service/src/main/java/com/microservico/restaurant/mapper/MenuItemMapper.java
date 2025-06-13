package com.microservico.restaurant.mapper;

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
        MenuItemResponseDTO dto = new MenuItemResponseDTO();

        dto.setId(menuItem.getId());
        dto.setNome(menuItem.getNome());
        dto.setPreco(menuItem.getPreco());
        dto.setRestaurantId(menuItem.getRestaurant().getId());
        dto.setAtivo(menuItem.getAtivo());

        return dto;
    }

}
