package com.microservico.customerservice.mapper;


import com.microservico.customerservice.dto.request.ClienteDtoRequest;
import com.microservico.customerservice.dto.response.ClienteDtoResponse;
import com.microservico.customerservice.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public static Cliente toEntity(ClienteDtoRequest dto) {
        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());

        return cliente;
    }

    public static ClienteDtoResponse toDto(Cliente cliente) {
        return new ClienteDtoResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail()
        );
    }

}
