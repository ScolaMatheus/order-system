package com.microservico.order.controller;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.service.PedidoService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

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
