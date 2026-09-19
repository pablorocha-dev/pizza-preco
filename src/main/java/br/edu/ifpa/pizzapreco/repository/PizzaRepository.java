package br.edu.ifpa.pizzapreco.repository;

import br.edu.ifpa.pizzapreco.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    boolean existsByNomeAndTamanho(String nome, String tamanho);

    boolean existsByNomeAndTamanhoAndIdPizzaNot(
            String nome,
            String tamanho,
            Long idPizza
    );

}