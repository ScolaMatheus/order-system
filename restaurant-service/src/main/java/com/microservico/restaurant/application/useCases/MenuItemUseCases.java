package com.microservico.restaurant.application.useCases;

import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;

import java.util.List;

public interface MenuItemUseCases {

    MenuItemResponseDTO cadastrar(MenuItemRequestDTO requestDTO);
    List<MenuItemResponseDTO> listarPorRestaurante(Long restaurantId);
    MenuItemResponseDTO buscarPorId(Long id);
    MenuItemResponseDTO atualizar(MenuItemRequestDTO dto, Long id);
    void excluir(Long id);

}
