package com.microservico.restaurant.controller;

import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.service.PedidoService;
import com.microservico.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestaurantController {


    private final RestaurantService restaurantService;
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> cadastrar(@Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> listar() {
        return ResponseEntity.ok(restaurantService.listarTodos());
    }

    @PostMapping("/pedidos/{idPedido}/em-rota")
    public ResponseEntity<PedidoStatusEvent> informarPedidoPronto(@PathVariable Long idPedido) {
        PedidoStatusEvent pedidoEvent = pedidoService.informarPedidoPronto(idPedido);

        return ResponseEntity.ok(pedidoEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.buscarPorId(id));
    }

    @GetMapping("/pedidos/restaurante/{idRestaurante}")
    public ResponseEntity<List<PedidoDtoResponse>> buscarPedidosPorRestaurante(@PathVariable Long idRestaurante) {
        return ResponseEntity.ok(pedidoService.buscarPedidoPorRestaurante(idRestaurante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.atualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restaurantService.excluir(id);
        return  ResponseEntity.noContent().build();
    }

}
