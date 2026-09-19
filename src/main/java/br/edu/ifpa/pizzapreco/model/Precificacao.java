package br.edu.ifpa.pizzapreco.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public BigDecimal calcularMargem() {
        if (custoTotal == null || lucro == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal precoVenda = lucro.add(custoTotal);

        if (precoVenda.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return lucro
                .divide(precoVenda, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
