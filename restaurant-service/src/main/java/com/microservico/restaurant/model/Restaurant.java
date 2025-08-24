package com.microservico.restaurant.model;

import java.util.List;

public class Restaurant {

    private Long id;

    private String nome;

    private String endereco;

    private Boolean ativo;

    private List<MenuItem> cardapio;

    public Restaurant() {
    }

    public Restaurant(Long id, String nome, String endereco, Boolean ativo, List<MenuItem> cardapio) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.ativo = ativo;
        this.cardapio = cardapio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<MenuItem> getCardapio() {
        return cardapio;
    }

    public void setCardapio(List<MenuItem> cardapio) {
        this.cardapio = cardapio;
    }
}
