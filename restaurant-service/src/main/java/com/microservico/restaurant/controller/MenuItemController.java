package com.microservico.restaurant.controller;

import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;
import com.microservico.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardapio")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> cadastrar(@Valid @RequestBody MenuItemRequestDTO dto) {
        return ResponseEntity.ok(menuItemService.cadastrar(dto));
    }

    @GetMapping("/restaurante/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDTO>> listarPorRestaurante(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.listarPorRestaurante(restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MenuItemRequestDTO dto) {
        return ResponseEntity.ok(menuItemService.atualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        menuItemService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
