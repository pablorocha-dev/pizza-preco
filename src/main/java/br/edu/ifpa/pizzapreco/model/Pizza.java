package br.edu.ifpa.pizzapreco.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPizza;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @OneToMany(
            mappedBy = "pizza",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ComposicaoPizza> composicoes = new ArrayList<>();

    public Pizza() {
    }

    public Pizza(String nome, BigDecimal precoVenda) {
        this.nome = nome;
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
}
