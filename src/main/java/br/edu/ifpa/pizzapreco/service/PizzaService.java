package br.edu.ifpa.pizzapreco.service;

import br.edu.ifpa.pizzapreco.model.ComposicaoPizza;
import br.edu.ifpa.pizzapreco.model.Insumo;
import br.edu.ifpa.pizzapreco.model.Pizza;
import br.edu.ifpa.pizzapreco.model.Precificacao;
import br.edu.ifpa.pizzapreco.repository.InsumoRepository;
import br.edu.ifpa.pizzapreco.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final InsumoRepository insumoRepository;

    public PizzaService(
            PizzaRepository pizzaRepository,
            InsumoRepository insumoRepository) {

        this.pizzaRepository = pizzaRepository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public Pizza cadastrar(Pizza pizza) {

        validarPizza(pizza);

        boolean jaExiste =
                pizzaRepository.existsByNomeAndTamanho(
                        pizza.getNome(),
                        pizza.getTamanho()
                );

        if (jaExiste) {
            throw new IllegalArgumentException(
                    "Já existe uma pizza com este nome e tamanho."
            );
        }

        return pizzaRepository.save(pizza);
    }

    @Transactional
    public Pizza editar(Pizza pizza) {

        if (pizza == null || pizza.getIdPizza() == null) {
            throw new IllegalArgumentException(
                    "A pizza precisa possuir um ID para ser editada."
            );
        }

        if (!pizzaRepository.existsById(pizza.getIdPizza())) {
            throw new IllegalArgumentException(
                    "Pizza não encontrada."
            );
        }

        validarPizza(pizza);

        boolean jaExisteOutra =
                pizzaRepository
                        .existsByNomeAndTamanhoAndIdPizzaNot(
                                pizza.getNome(),
                                pizza.getTamanho(),
                                pizza.getIdPizza()
                        );

        if (jaExisteOutra) {
            throw new IllegalArgumentException(
                    "Já existe outra pizza com este nome e tamanho."
            );
        }

        return pizzaRepository.save(pizza);
    }

    @Transactional(readOnly = true)
    public List<Pizza> listar() {
        return pizzaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pizza buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "O ID da pizza é obrigatório."
            );
        }

        return pizzaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pizza não encontrada."
                        )
                );
    }

    @Transactional
    public Pizza informarPreco(
            Long idPizza,
            BigDecimal preco) {

        Pizza pizza = buscarPorId(idPizza);

        if (preco == null ||
                preco.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "O preço de venda deve ser válido e não negativo."
            );
        }

        pizza.setPrecoVenda(preco);

        return pizzaRepository.save(pizza);
    }

    @Transactional
    public Pizza compor(
            Long idPizza,
            Long idInsumo,
            BigDecimal quantidade) {

        Pizza pizza = buscarPorId(idPizza);
        Insumo insumo = buscarInsumo(idInsumo);

        validarQuantidade(quantidade);

        boolean jaExiste = pizza.getComposicoes()
                .stream()
                .anyMatch(composicao ->
                        composicao.getInsumo()
                                .getIdInsumo()
                                .equals(idInsumo)
                );

        if (jaExiste) {
            throw new IllegalArgumentException(
                    "Este insumo já está presente na composição da pizza."
            );
        }

        ComposicaoPizza composicao =
                new ComposicaoPizza();

        composicao.setInsumo(insumo);
        composicao.setQuantidade(quantidade);

        pizza.adicionarComposicao(composicao);

        return pizzaRepository.save(pizza);
    }

    @Transactional
    public Pizza alterarComposicao(
            Long idPizza,
            Long idComposicao,
            BigDecimal novaQuantidade) {

        Pizza pizza = buscarPorId(idPizza);

        validarQuantidade(novaQuantidade);

        ComposicaoPizza composicao =
                pizza.getComposicoes()
                        .stream()
                        .filter(c ->
                                c.getIdComposicao()
                                        .equals(idComposicao)
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Composição não encontrada."
                                )
                        );

        composicao.alterarQuantidade(novaQuantidade);

        return pizzaRepository.save(pizza);
    }

    @Transactional
    public Pizza removerComposicao(
            Long idPizza,
            Long idComposicao) {

        Pizza pizza = buscarPorId(idPizza);

        ComposicaoPizza composicao =
                pizza.getComposicoes()
                        .stream()
                        .filter(c ->
                                c.getIdComposicao()
                                        .equals(idComposicao)
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Composição não encontrada."
                                )
                        );

        pizza.removerComposicao(composicao);

        return pizzaRepository.save(pizza);
    }

    @Transactional(readOnly = true)
    public Precificacao consultarRentabilidade(Long idPizza) {

        Pizza pizza = buscarPorId(idPizza);

        if (pizza.getComposicoes() == null ||
                pizza.getComposicoes().isEmpty()) {

            throw new IllegalStateException(
                    "A pizza não possui composição."
            );
        }

        BigDecimal custoTotal =
                calcularCusto(pizza);

        if (pizza.getPrecoVenda() == null) {

            return new Precificacao(
                    custoTotal,
                    null,
                    null
            );
        }

        BigDecimal lucro =
                pizza.getPrecoVenda()
                        .subtract(custoTotal)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal margem;

        if (pizza.getPrecoVenda()
                .compareTo(BigDecimal.ZERO) == 0) {

            margem = BigDecimal.ZERO;

        } else {

            margem = lucro
                    .divide(
                            pizza.getPrecoVenda(),
                            4,
                            RoundingMode.HALF_UP
                    )
                    .multiply(
                            BigDecimal.valueOf(100)
                    )
                    .setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return new Precificacao(
                custoTotal,
                lucro,
                margem
        );
    }

    private BigDecimal calcularCusto(Pizza pizza) {

        BigDecimal custoTotal =
                BigDecimal.ZERO;

        for (ComposicaoPizza composicao :
                pizza.getComposicoes()) {

            Insumo insumo =
                    composicao.getInsumo();

            if (insumo == null ||
                    insumo.getPrecoUnitario() == null) {

                throw new IllegalStateException(
                        "Existe um insumo sem preço válido."
                );
            }

            BigDecimal custoInsumo =
                    insumo.getPrecoUnitario()
                            .multiply(
                                    composicao.getQuantidade()
                            );

            custoTotal =
                    custoTotal.add(custoInsumo);
        }

        return custoTotal.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private Insumo buscarInsumo(Long idInsumo) {

        if (idInsumo == null) {
            throw new IllegalArgumentException(
                    "O ID do insumo é obrigatório."
            );
        }

        return insumoRepository.findById(idInsumo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Insumo não encontrado."
                        )
                );
    }

    private void validarQuantidade(
            BigDecimal quantidade) {

        if (quantidade == null ||
                quantidade.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }
    }

    private void validarPizza(Pizza pizza) {

        if (pizza == null) {
            throw new IllegalArgumentException(
                    "A pizza não pode ser nula."
            );
        }

        if (pizza.getNome() == null ||
                pizza.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome da pizza é obrigatório."
            );
        }

        if (pizza.getTamanho() == null ||
                pizza.getTamanho().isBlank()) {

            throw new IllegalArgumentException(
                    "O tamanho da pizza é obrigatório."
            );
        }

        if (pizza.getPrecoVenda() != null &&
                pizza.getPrecoVenda()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "O preço de venda não pode ser negativo."
            );
        }
    }
}