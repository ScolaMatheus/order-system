package com.microservico.restaurantService.service;

import com.microservico.restaurantService.dto.request.RestaurantRequestDTO;
import com.microservico.restaurantService.dto.response.RestaurantResponseDTO;
import com.microservico.restaurantService.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurantService.mapper.RestaurantMapper;
import com.microservico.restaurantService.model.Restaurant;
import com.microservico.restaurantService.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantResponseDTO cadastrar(RestaurantRequestDTO dto) {
        Restaurant restaurant = RestaurantMapper.toEntity(dto);

        return RestaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    public List<RestaurantResponseDTO> listarTodos() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO buscarPorId(Long id) {
        return restaurantRepository.findById(id).map(RestaurantMapper::toDto).orElseThrow(
                () -> new RecursoNaoEncontradoException("Nenhum restaurante encontrado com esse id: " + id));
    }

    public RestaurantResponseDTO atualizar(RestaurantRequestDTO dto, Long id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setNome(dto.getNome());
                    restaurant.setEndereco(dto.getEndereco());
                    restaurant.setAtivo(dto.isAtivo());
                    return RestaurantMapper.toDto(restaurantRepository.save(restaurant));
                }).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum restaurante encontrado com esse id: " + id));
    }

    public void excluir(Long id) {
         restaurantRepository.findById(id).ifPresentOrElse(
                 restaurantRepository::delete,
                 () -> {throw new RecursoNaoEncontradoException("Nenhum restaurante encontrado com esse id: " + id); }
         );
    }

}
