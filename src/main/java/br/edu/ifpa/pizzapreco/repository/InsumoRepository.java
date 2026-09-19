package br.edu.ifpa.pizzapreco.repository;

import br.edu.ifpa.pizzapreco.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

}