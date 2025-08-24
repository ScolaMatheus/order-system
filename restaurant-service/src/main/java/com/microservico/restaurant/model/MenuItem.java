package com.microservico.restaurant.model;

import java.math.BigDecimal;

public class MenuItem {

    private Long id;

    private String nome;

    private BigDecimal preco;

    private Restaurant restaurant;

    private Boolean ativo;

    public MenuItem() {
    }

    public MenuItem(Long id, String nome, BigDecimal preco, Restaurant restaurant, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.restaurant = restaurant;
        this.ativo = ativo;
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
