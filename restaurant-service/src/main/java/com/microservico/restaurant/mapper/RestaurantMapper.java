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
        RestaurantResponseDTO dto = new RestaurantResponseDTO();

        dto.setId(restaurant.getId());
        dto.setNome(restaurant.getNome());
        dto.setEndereco(restaurant.getEndereco());
        dto.setAtivo(restaurant.getAtivo());

        return dto;
    }
}
