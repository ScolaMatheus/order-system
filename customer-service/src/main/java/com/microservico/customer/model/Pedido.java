package com.microservico.customer.model;

import com.microservico.customer.util.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private Long id;

    private Long clienteId;

    private Long restauranteId;

    private List<ItemPedido> itens;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    private StatusPedido statusPedido;

    private BigDecimal valorTotal;

    public BigDecimal getValorTotal() {
        if (itens == null) return new BigDecimal(0);
        return itens.stream()
                .map(ItemPedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Pedido() {}

    public Pedido(Long id, Long clienteId, Long restauranteId, List<ItemPedido> itens, LocalDateTime dataCriacao,
                  LocalDateTime dataAtualizacao, StatusPedido statusPedido, BigDecimal valorTotal) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.itens = itens;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.statusPedido = statusPedido;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
