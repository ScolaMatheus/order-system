package com.microservico.order.model;

import java.math.BigDecimal;

public class ItemPedido {

    private Long id;

    private Pedido pedido;

    private Long produtoId;

    private String nomeProduto;

    private BigDecimal precoUnitario;

    private Integer quantidade;


    public ItemPedido() {}

    public ItemPedido(Long id, Pedido pedido, Long produtoId, String nomeProduto, BigDecimal precoUnitario, Integer quantidade) {
        this.id = id;
        this.pedido = pedido;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        if (precoUnitario == null || quantidade == null) return BigDecimal.ZERO;
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
