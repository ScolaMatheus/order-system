package com.microservico.order.adapter.inbound.controller;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.application.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos (Order Management)", description = "Gestão dos pedidos criados")
@RestController
@RequestMapping("/api/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Buscar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<PedidoDtoResponse>> buscarTodosPedidos() {
        List<PedidoDtoResponse> pedidoDtoResponseList = pedidoService.buscarPedidos();

        return ResponseEntity.ok(pedidoDtoResponseList);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido especificado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("{idPedido}")
    public ResponseEntity<PedidoDtoResponse> buscarPedidoPorId(@PathVariable Long idPedido) {
        PedidoDtoResponse pedidoDtoResponse = pedidoService.buscarPedidoPorId(idPedido);

        return ResponseEntity.ok(pedidoDtoResponse);
    }

    @Operation(summary = "Excluir pedido", description = "Remove um pedido do sistema com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("{idPedido}")
    public ResponseEntity<String> excluirPedido(@PathVariable Long idPedido) {
        pedidoService.excluirPedido(idPedido);

        return ResponseEntity.ok("Pedido excluido com sucesso!");
    }
}
