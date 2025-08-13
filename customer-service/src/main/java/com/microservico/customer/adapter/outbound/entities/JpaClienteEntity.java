package com.microservico.customer.adapter.outbound.entities;

import com.microservico.customer.model.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "tb_cliente")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class JpaClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    @Column(length = 11, unique = true, nullable = false)
    private String cpf;

    @Column(length = 15, nullable = false)
    private String telefone;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    public JpaClienteEntity(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
    }

}
