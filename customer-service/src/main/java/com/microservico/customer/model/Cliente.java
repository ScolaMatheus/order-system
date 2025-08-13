package com.microservico.customer.model;

public class Cliente {

    private Long id;

    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    public Cliente() {}

    public Cliente(Long id, String email, String nome, String cpf, String telefone) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
