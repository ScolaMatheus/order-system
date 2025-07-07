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
        return new MenuItemResponseDTO(
                menuItem.getId(),
                menuItem.getNome(),
                menuItem.getPreco(),
                menuItem.getRestaurant().getId(),
                menuItem.getAtivo()
        );
    }
}
