package com.microservico.customerservice.controller;


import com.microservico.customerservice.dto.request.ClienteDtoRequest;
import com.microservico.customerservice.dto.request.PedidoDtoRequest;
import com.microservico.customerservice.dto.response.ClienteDtoResponse;
import com.microservico.customerservice.dto.response.PedidoDtoResponse;
import com.microservico.customerservice.service.ClienteService;
import com.microservico.customerservice.service.PedidoClient;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoClient pedidoClient;

    @PostMapping()
    public ResponseEntity<ClienteDtoResponse> cadastrarCliente(@Valid @RequestBody ClienteDtoRequest dto) {
        ClienteDtoResponse clienteDtoResponse = clienteService.cadastrar(dto);

        URI uri = URI.create("/clientes/" + clienteDtoResponse.getId());
        return ResponseEntity.created(uri).body(clienteDtoResponse);
    }

    @PostMapping("/{id}/pedidos")
    public ResponseEntity<PedidoDtoResponse> fazerPedido(@RequestBody PedidoDtoRequest request) {
        PedidoDtoResponse pedidoDtoResponse = pedidoClient.criarPedido(request);

        URI uri = URI.create("/api/pedidos/" + pedidoDtoResponse.getId());
        return ResponseEntity.created(uri).body(pedidoDtoResponse);
    }

    @GetMapping()
    public ResponseEntity<List<ClienteDtoResponse>> buscarCliente() {
        List<ClienteDtoResponse> clienteDtoResponseList = clienteService.buscarClientes();

        return ResponseEntity.ok(clienteDtoResponseList);
    }

    @GetMapping("{idCliente}")
    public ResponseEntity<ClienteDtoResponse> buscarClientePorId(@PathVariable Long idCliente){
        ClienteDtoResponse clienteDtoResponse = clienteService.buscarClientePorId(idCliente);

        return ResponseEntity.ok(clienteDtoResponse);
    }

    @PatchMapping("{idCliente}")
    public ResponseEntity<ClienteDtoResponse> atualizar(@PathVariable Long idCliente, @RequestBody ClienteDtoRequest dtoRequest) {
        ClienteDtoResponse clienteDtoResponse = clienteService.atualizar(dtoRequest, idCliente);

        return ResponseEntity.ok(clienteDtoResponse);
    }

    @DeleteMapping("{idCliente}")
    public ResponseEntity<String> excluirCliente(@PathVariable Long idCliente) {
        clienteService.excluirCliente(idCliente);

        return ResponseEntity.ok("Cliente excluído com sucesso");
    }

}
