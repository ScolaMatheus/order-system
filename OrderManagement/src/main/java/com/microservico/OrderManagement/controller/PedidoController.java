package com.microservico.OrderManagement.controller;

import com.microservico.OrderManagement.dto.request.PedidoDtoRequest;
import com.microservico.OrderManagement.dto.response.PedidoDtoResponse;
import com.microservico.OrderManagement.service.PedidoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping()
    public ResponseEntity<PedidoDtoResponse> criarPedido(@Valid @RequestBody PedidoDtoRequest dtoRequest) {
        PedidoDtoResponse pedidoDtoResponse = pedidoService.criarPedido(dtoRequest);

        URI uri = URI.create("/api/pedidos/" + pedidoDtoResponse.getId());
        return ResponseEntity.created(uri).body(pedidoDtoResponse);
    }

    @GetMapping()
    public ResponseEntity<List<PedidoDtoResponse>> buscarTodosPedidos() {
        List<PedidoDtoResponse> pedidoDtoResponseList = pedidoService.buscarPedidos();

        return ResponseEntity.ok(pedidoDtoResponseList);
    }

    @GetMapping("{idPedido}")
    public ResponseEntity<PedidoDtoResponse> buscarPedidoPorId(@PathVariable Long idPedido) {
        PedidoDtoResponse pedidoDtoResponse = pedidoService.buscarPedidoPorId(idPedido);

        return ResponseEntity.ok(pedidoDtoResponse);
    }

    @DeleteMapping("{idPedido}")
    public ResponseEntity<String> excluirPedido(@PathVariable Long idPedido) {
        pedidoService.excluirPedido(idPedido);

        return ResponseEntity.ok("Pedido excluido com sucesso!");
    }
}
