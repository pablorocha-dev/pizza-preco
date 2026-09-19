package br.edu.ifpa.pizzapreco.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "composicao_pizza",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pizza_insumo",
                        columnNames = {"id_pizza", "id_insumo"}
                )
        }
)
public class ComposicaoPizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComposicao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pizza", nullable = false)
    private Pizza pizza;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    public ComposicaoPizza() {
    }

    public ComposicaoPizza(Pizza pizza, Insumo insumo, BigDecimal quantidade) {
        this.pizza = pizza;
        this.insumo = insumo;
        this.quantidade = quantidade;
    }

    public Long getIdComposicao() {
        return idComposicao;
    }

    public void setIdComposicao(Long idComposicao) {
        this.idComposicao = idComposicao;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public void alterarQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ComposicaoPizza that = (ComposicaoPizza) o;
        return Objects.equals(idComposicao, that.idComposicao);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idComposicao);
    }
}