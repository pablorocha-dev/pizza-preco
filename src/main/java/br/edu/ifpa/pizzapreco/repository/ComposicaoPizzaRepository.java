package br.edu.ifpa.pizzapreco.repository;

import br.edu.ifpa.pizzapreco.model.ComposicaoPizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposicaoPizzaRepository extends JpaRepository<ComposicaoPizza, Long> {

}