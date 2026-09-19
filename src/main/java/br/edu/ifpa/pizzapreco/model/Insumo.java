package br.edu.ifpa.pizzapreco.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "insumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private String unidadeMedida;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public Insumo() {
    }

    public Insumo(String nome, String unidadeMedida, BigDecimal precoUnitario) {
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.precoUnitario = precoUnitario;
    }

    public Long getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
