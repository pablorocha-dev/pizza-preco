package br.edu.ifpa.pizzapreco.service;

import br.edu.ifpa.pizzapreco.model.Insumo;
import br.edu.ifpa.pizzapreco.repository.InsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public Insumo cadastrar(Insumo insumo) {

        validarInsumo(insumo);

        return insumoRepository.save(insumo);
    }

    @Transactional
    public Insumo editar(Insumo insumo) {

        if (insumo == null || insumo.getIdInsumo() == null) {
            throw new IllegalArgumentException(
                    "O insumo precisa possuir um ID para ser editado."
            );
        }

        if (!insumoRepository.existsById(insumo.getIdInsumo())) {
            throw new IllegalArgumentException(
                    "Insumo não encontrado."
            );
        }

        validarInsumo(insumo);

        return insumoRepository.save(insumo);
    }

    @Transactional(readOnly = true)
    public List<Insumo> listar() {
        return insumoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Insumo buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "O ID do insumo é obrigatório."
            );
        }

        return insumoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Insumo não encontrado."
                        )
                );
    }

    private void validarInsumo(Insumo insumo) {

        if (insumo == null) {
            throw new IllegalArgumentException(
                    "O insumo não pode ser nulo."
            );
        }

        if (insumo.getNome() == null ||
                insumo.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome do insumo é obrigatório."
            );
        }

        if (insumo.getUnidadeMedida() == null ||
                insumo.getUnidadeMedida().isBlank()) {

            throw new IllegalArgumentException(
                    "A unidade de medida é obrigatória."
            );
        }

        if (insumo.getPrecoUnitario() == null ||
                insumo.getPrecoUnitario()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "O preço do insumo deve ser válido e não negativo."
            );
        }
    }
}