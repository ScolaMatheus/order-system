package com.microservico.customer.controller;


import com.microservico.customer.dto.request.ClienteDtoRequest;
import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.ClienteDtoResponse;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.service.ClienteService;
import com.microservico.customer.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Operações relacionadas a clientes e seus pedidos")
@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping()
    public ResponseEntity<ClienteDtoResponse> cadastrarCliente(@Valid @RequestBody ClienteDtoRequest dto) {
        ClienteDtoResponse clienteDtoResponse = clienteService.cadastrar(dto);

        URI uri = URI.create("/clientes/" + clienteDtoResponse.id());
        return ResponseEntity.created(uri).body(clienteDtoResponse);
    }

    @Operation(summary = "Realizar pedido", description = "Cria um novo pedido para um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDtoResponse> fazerPedido(@RequestBody PedidoDtoRequest request) {
        PedidoDtoResponse pedidoDtoResponse = pedidoService.criarPedidoEvent(request);

        URI uri = URI.create("/api/pedidos/" + pedidoDtoResponse.id());
        return ResponseEntity.created(uri).body(pedidoDtoResponse);
    }

    @Operation(summary = "Informar entrega de pedido", description = "Atualiza o status do pedido para entregue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido marcado como entregue"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PatchMapping("/pedidos/{idPedido}/entrega")
    public ResponseEntity<PedidoStatusEvent> informarPedidoEntregue(@PathVariable Long idPedido) {
        PedidoStatusEvent pedidoStatusEvent = pedidoService.informarPedidoEntregue(idPedido);

        return ResponseEntity.ok(pedidoStatusEvent);
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PostMapping("pedidos/{idPedido}/cancelamento")
    public ResponseEntity<PedidoCanceladoEvent> cancelarPedido(@PathVariable Long idPedido) {
        PedidoCanceladoEvent canceladoEvent = pedidoService.processarCancelamentoDePedido(idPedido);

        return ResponseEntity.ok(canceladoEvent);
    }

    @Operation(summary = "Buscar todos os clientes", description = "Retorna a lista de todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<ClienteDtoResponse>> buscarCliente() {
        List<ClienteDtoResponse> clienteDtoResponseList = clienteService.buscarClientes();

        return ResponseEntity.ok(clienteDtoResponseList);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("{idCliente}")
    public ResponseEntity<ClienteDtoResponse> buscarClientePorId(@PathVariable Long idCliente){
        ClienteDtoResponse clienteDtoResponse = clienteService.buscarClientePorId(idCliente);

        return ResponseEntity.ok(clienteDtoResponse);
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PatchMapping("{idCliente}")
    public ResponseEntity<ClienteDtoResponse> atualizar(@PathVariable Long idCliente, @RequestBody ClienteDtoRequest dtoRequest) {
        ClienteDtoResponse clienteDtoResponse = clienteService.atualizar(dtoRequest, idCliente);

        return ResponseEntity.ok(clienteDtoResponse);
    }

    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("{idCliente}")
    public ResponseEntity<String> excluirCliente(@PathVariable Long idCliente) {
        clienteService.excluirCliente(idCliente);

        return ResponseEntity.ok("Cliente excluído com sucesso");
    }

}
