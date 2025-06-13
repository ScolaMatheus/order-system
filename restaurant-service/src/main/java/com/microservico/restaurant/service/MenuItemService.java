package com.microservico.restaurant.service;

import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.mapper.MenuItemMapper;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.repositories.MenuItemRepository;
import com.microservico.restaurant.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;


    public MenuItemResponseDTO cadastrar(MenuItemRequestDTO requestDTO)  {
        Restaurant restaurant = getRestaurant(requestDTO.getRestaurantId());

        MenuItem menuItem = MenuItemMapper.toEntity(requestDTO, restaurant);

        return MenuItemMapper.toDto(menuItemRepository.save(menuItem));
    }

    public List<MenuItemResponseDTO> listarPorRestaurante(Long restaurantId) {
        getRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public MenuItemResponseDTO buscarPorId(Long id) {
        return menuItemRepository.findById(id).map(MenuItemMapper::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum cardápio encontrado com esse id: " + id));
    }

    public MenuItemResponseDTO atualizar(MenuItemRequestDTO dto, Long id) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    if (Objects.nonNull(dto.getNome()))menuItem.setNome(dto.getNome());

                    if (Objects.nonNull(dto.getPreco())) menuItem.setPreco(dto.getPreco());

                    if (Objects.nonNull(dto.getAtivo())) menuItem.setAtivo(dto.getAtivo());

                    if (Objects.nonNull(dto.getRestaurantId())) menuItem.setRestaurant(getRestaurant(dto.getRestaurantId()));

                    menuItemRepository.save(menuItem);
                    return MenuItemMapper.toDto(menuItem);
                }).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum cardápio encontrado com esse id: " + id));
    }

    public void excluir(Long id) {
        menuItemRepository.findById(id).ifPresentOrElse(
                menuItemRepository::delete,
                () -> { throw new RecursoNaoEncontradoException("Nenhum cardápio encontrado com esse id: " + id); }
        );
    }

    private Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum restaurante encontrado com esse id: " + restaurantId));
    }

}
