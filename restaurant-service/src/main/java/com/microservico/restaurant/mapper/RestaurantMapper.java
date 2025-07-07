package com.microservico.restaurant.mapper;

import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;
import com.microservico.restaurant.model.Restaurant;

public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant();

        restaurant.setNome(dto.getNome());
        restaurant.setEndereco(dto.getEndereco());
        restaurant.setAtivo(dto.isAtivo());

        return restaurant;
    }

    public static RestaurantResponseDTO toDto(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getNome(),
                restaurant.getEndereco(),
                restaurant.getAtivo()
        );
    }
}
