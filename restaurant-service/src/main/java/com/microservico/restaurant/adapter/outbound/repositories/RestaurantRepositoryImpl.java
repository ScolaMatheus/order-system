package com.microservico.restaurant.adapter.outbound.repositories;

import com.microservico.restaurant.adapter.outbound.entities.JpaRestaurantEntity;
import com.microservico.restaurant.application.repository.RestaurantRepository;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.util.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final JpaRestaurantRepository jpaRestaurantRepository;

    @Override
    public Restaurant save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurant = RestaurantMapper.toJpaEntity(restaurant);
        return RestaurantMapper.toEntity(this.jpaRestaurantRepository.save(jpaRestaurant));
    }

    @Override
    public List<Restaurant> findAll() {
        return this.jpaRestaurantRepository.findAll()
                .stream()
                .map(RestaurantMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<Restaurant> findById(Long idRestaurant) {
        Optional<JpaRestaurantEntity> restaurant = this.jpaRestaurantRepository.findById(idRestaurant);
        return Optional.of(restaurant.map(RestaurantMapper::toEntity)).orElseThrow();
    }

    @Override
    public void delete(Restaurant restaurant) {
        this.jpaRestaurantRepository.delete(RestaurantMapper.toJpaEntity(restaurant));
    }
}
