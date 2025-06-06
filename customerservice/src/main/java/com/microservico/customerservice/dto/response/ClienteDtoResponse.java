package com.microservico.customerservice.dto.response;

import com.microservico.customerservice.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClienteDtoResponse {
    private final Long id;

    private final String nome;

    private final String cpf;

    private final String telefone;

    private final String email;

    public ClienteDtoResponse(Cliente clienteCadastrado) {
        this.id = clienteCadastrado.getId();
        this.nome = clienteCadastrado.getNome();
        this.cpf = clienteCadastrado.getCpf();
        this.telefone = clienteCadastrado.getTelefone();
        this.email = clienteCadastrado.getEmail();
    }
}
