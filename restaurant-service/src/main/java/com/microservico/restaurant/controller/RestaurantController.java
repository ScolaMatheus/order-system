package com.microservico.restaurant.controller;

import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.service.PedidoService;
import com.microservico.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurantes", description = "Gerenciamento dos restaurantes e seus pedidos")
@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestaurantController {


    private final RestaurantService restaurantService;
    private final PedidoService pedidoService;

    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> cadastrar(@Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.cadastrar(dto));
    }

    @Operation(summary = "Listar restaurantes", description = "Retorna a lista de todos os restaurantes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> listar() {
        return ResponseEntity.ok(restaurantService.listarTodos());
    }

    @Operation(summary = "Informar pedido pronto para entrega", description = "Atualiza o status do pedido para 'em rota' quando estiver pronto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado para 'em rota'"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PostMapping("/pedidos/{idPedido}/em-rota")
    public ResponseEntity<PedidoStatusEvent> informarPedidoPronto(@PathVariable Long idPedido) {
        PedidoStatusEvent pedidoEvent = pedidoService.informarPedidoPronto(idPedido);

        return ResponseEntity.ok(pedidoEvent);
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados de um restaurante com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.buscarPorId(id));
    }

    @Operation(summary = "Buscar pedidos por restaurante", description = "Retorna todos os pedidos vinculados ao restaurante especificado")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso")
    @GetMapping("/pedidos/restaurante/{idRestaurante}")
    public ResponseEntity<List<PedidoDtoResponse>> buscarPedidosPorRestaurante(@PathVariable Long idRestaurante) {
        return ResponseEntity.ok(pedidoService.buscarPedidoPorRestaurante(idRestaurante));
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.atualizar(dto, id));
    }

    @Operation(summary = "Excluir restaurante", description = "Remove um restaurante com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restaurantService.excluir(id);
        return  ResponseEntity.noContent().build();
    }

}
