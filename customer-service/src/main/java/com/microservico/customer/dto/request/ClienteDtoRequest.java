package com.microservico.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClienteDtoRequest {
    @Size(min = 3, max = 255, message = "Nome deve no mínimo 3 caracteres")
    private String nome;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @Pattern(regexp = "\\d{13}", message = "Telefone deve conter 15 dígitos numéricos")
    private String telefone;

    @Email(message = "E-mail deve ser válido")
    @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
    private String email;
}
