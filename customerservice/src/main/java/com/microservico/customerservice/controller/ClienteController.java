package com.microservico.customerservice.controller;


import com.microservico.customerservice.dto.request.ClienteDtoRequest;
import com.microservico.customerservice.dto.request.PedidoDtoRequest;
import com.microservico.customerservice.dto.response.ClienteDtoResponse;
import com.microservico.customerservice.dto.response.PedidoDtoResponse;
import com.microservico.customerservice.event.PedidoCanceladoEvent;
import com.microservico.customerservice.event.PedidoStatusEvent;
import com.microservico.customerservice.service.ClienteService;
import com.microservico.customerservice.service.PedidoService;
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
    private final PedidoService pedidoService;

    @PostMapping()
    public ResponseEntity<ClienteDtoResponse> cadastrarCliente(@Valid @RequestBody ClienteDtoRequest dto) {
        ClienteDtoResponse clienteDtoResponse = clienteService.cadastrar(dto);

        URI uri = URI.create("/clientes/" + clienteDtoResponse.id());
        return ResponseEntity.created(uri).body(clienteDtoResponse);
    }

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDtoResponse> fazerPedido(@RequestBody PedidoDtoRequest request) {
        PedidoDtoResponse pedidoDtoResponse = pedidoService.criarPedidoEvent(request);

        URI uri = URI.create("/api/pedidos/" + pedidoDtoResponse.id());
        return ResponseEntity.created(uri).body(pedidoDtoResponse);
    }

    @PatchMapping("/pedidos/{idPedido}/entrega")
    public ResponseEntity<PedidoStatusEvent> informarPedidoEntregue(@PathVariable Long idPedido) {
        PedidoStatusEvent pedidoStatusEvent = pedidoService.informarPedidoEntregue(idPedido);

        return ResponseEntity.ok(pedidoStatusEvent);
    }

    @PostMapping("pedidos/{idPedido}/cancelamento")
    public ResponseEntity<PedidoCanceladoEvent> cancelarPedido(@PathVariable Long idPedido) {
        PedidoCanceladoEvent canceladoEvent = pedidoService.processarCancelamentoDePedido(idPedido);

        return ResponseEntity.ok(canceladoEvent);
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
