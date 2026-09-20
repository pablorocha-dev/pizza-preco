package br.edu.ifpa.pizzapreco.model;

import java.math.BigDecimal;

public class Precificacao {

    private BigDecimal custoTotal;
    private BigDecimal lucro;
    private BigDecimal margemLucro;

    public Precificacao() {
    }

    public Precificacao(BigDecimal custoTotal,
                        BigDecimal lucro,
                        BigDecimal margemLucro) {
        this.custoTotal = custoTotal;
        this.lucro = lucro;
        this.margemLucro = margemLucro;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    public BigDecimal getLucro() {
        return lucro;
    }

    public void setLucro(BigDecimal lucro) {
        this.lucro = lucro;
    }

    public BigDecimal getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(BigDecimal margemLucro) {
        this.margemLucro = margemLucro;
    }
}
