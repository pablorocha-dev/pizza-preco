package br.edu.ifpa.pizzapreco.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "pizza",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pizza_nome_tamanho",
                        columnNames = {"nome", "tamanho"}
                )
        }
)
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPizza;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 30)
    private String tamanho;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @OneToMany(
            mappedBy = "pizza",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ComposicaoPizza> composicoes = new ArrayList<>();

    public Pizza() {
    }

    public Pizza(String nome, String tamanho, BigDecimal precoVenda) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.precoVenda = precoVenda;
    }

    public Long getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(Long idPizza) {
        this.idPizza = idPizza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public List<ComposicaoPizza> getComposicoes() {
        return composicoes;
    }

    public void setComposicoes(List<ComposicaoPizza> composicoes) {
        this.composicoes = composicoes;
    }

    public void adicionarComposicao(ComposicaoPizza composicao) {
        composicoes.add(composicao);
        composicao.setPizza(this);
    }

    public void removerComposicao(ComposicaoPizza composicao) {
        composicoes.remove(composicao);
        composicao.setPizza(null);
    }

    public void alterarComposicao(ComposicaoPizza composicao) {
        if (!composicoes.contains(composicao)) {
            composicoes.add(composicao);
        }

        composicao.setPizza(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return Objects.equals(idPizza, pizza.idPizza);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPizza);
    }
}
