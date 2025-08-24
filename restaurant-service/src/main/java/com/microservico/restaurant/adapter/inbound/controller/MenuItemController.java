package com.microservico.restaurant.adapter.inbound.controller;

import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;
import com.microservico.restaurant.application.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Itens do Cardápio", description = "Gerenciamento dos itens do cardápio dos restaurantes")
@RestController
@RequestMapping("/cardapio")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Operation(summary = "Cadastrar item no cardápio", description = "Adiciona um novo item ao cardápio de um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> cadastrar(@Valid @RequestBody MenuItemRequestDTO dto) {
        return ResponseEntity.ok(menuItemService.cadastrar(dto));
    }

    @Operation(summary = "Listar itens por restaurante", description = "Retorna todos os itens do cardápio de um restaurante especificado")
    @ApiResponse(responseCode = "200", description = "Itens encontrados com sucesso")
    @GetMapping("/restaurante/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDTO>> listarPorRestaurante(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.listarPorRestaurante(restaurantId));
    }

    @Operation(summary = "Buscar item por ID", description = "Retorna os detalhes de um item do cardápio com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar item do cardápio", description = "Atualiza as informações de um item do cardápio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MenuItemRequestDTO dto) {
        return ResponseEntity.ok(menuItemService.atualizar(dto, id));
    }

    @Operation(summary = "Excluir item do cardápio", description = "Remove um item do cardápio com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        menuItemService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
