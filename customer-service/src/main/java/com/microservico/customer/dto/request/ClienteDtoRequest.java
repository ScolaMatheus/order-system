package com.microservico.customer.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClienteDtoRequest {
    private String nome;

    private String cpf;

    private String telefone;

    private String email;
}
