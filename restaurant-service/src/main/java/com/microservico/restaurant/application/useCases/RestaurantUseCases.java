package com.microservico.restaurant.application.useCases;

import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantUseCases {

    RestaurantResponseDTO cadastrar(RestaurantRequestDTO dto);
    List<RestaurantResponseDTO> listarTodos();
    RestaurantResponseDTO buscarPorId(Long id);
    RestaurantResponseDTO atualizar(RestaurantRequestDTO dto, Long id);
    void excluir(Long id);

}
