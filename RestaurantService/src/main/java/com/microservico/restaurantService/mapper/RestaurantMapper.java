package com.microservico.restaurantService.mapper;

import com.microservico.restaurantService.dto.request.RestaurantRequestDTO;
import com.microservico.restaurantService.dto.response.RestaurantResponseDTO;
import com.microservico.restaurantService.model.Restaurant;

public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant();

        restaurant.setNome(dto.getNome());
        restaurant.setEndereco(dto.getEndereco());
        restaurant.setAtivo(dto.isAtivo());

        return restaurant;
    }

    public static RestaurantResponseDTO toDto(Restaurant restaurant) {
        RestaurantResponseDTO dto = new RestaurantResponseDTO();

        dto.setId(restaurant.getId());
        dto.setNome(restaurant.getNome());
        dto.setEndereco(restaurant.getEndereco());
        dto.setAtivo(restaurant.isAtivo());

        return dto;
    }
}
