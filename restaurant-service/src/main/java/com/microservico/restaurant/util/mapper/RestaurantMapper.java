package com.microservico.restaurant.util.mapper;

import com.microservico.restaurant.adapter.outbound.entities.JpaMenuItemEntity;
import com.microservico.restaurant.adapter.outbound.entities.JpaRestaurantEntity;
import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

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

    public static Restaurant toEntity(JpaRestaurantEntity restaurantEntity) {
        Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantEntity.getId());
        restaurant.setNome(restaurantEntity.getNome());
        restaurant.setEndereco(restaurantEntity.getEndereco());
        restaurant.setAtivo(restaurantEntity.getAtivo());

        List<MenuItem> cardapio = restaurantEntity.getCardapio()
                .stream()
                .map(jpaMenuItemEntity -> MenuItemMapper.toEntity(jpaMenuItemEntity, restaurant))
                .toList();

        restaurant.setCardapio(cardapio);

        return restaurant;
    }

    public static JpaRestaurantEntity toJpaEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        JpaRestaurantEntity jpaRestaurant = new JpaRestaurantEntity();

        jpaRestaurant.setId(restaurant.getId());
        jpaRestaurant.setNome(restaurant.getNome());
        jpaRestaurant.setEndereco(restaurant.getEndereco());
        jpaRestaurant.setAtivo(restaurant.getAtivo());

        List<JpaMenuItemEntity> cardapio = restaurant.getCardapio()
                .stream()
                .map(jpaMenuItemEntity -> MenuItemMapper.toJpaEntity(jpaMenuItemEntity, jpaRestaurant))
                .toList();

        jpaRestaurant.setCardapio(cardapio);

        return jpaRestaurant;
    }

}
