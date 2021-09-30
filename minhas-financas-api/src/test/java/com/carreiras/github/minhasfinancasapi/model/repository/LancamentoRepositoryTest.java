package com.carreiras.github.minhasfinancasapi.model.repository;

import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import com.carreiras.github.minhasfinancasapi.model.enums.StatusLancamento;
import com.carreiras.github.minhasfinancasapi.model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Lancamento criarLancamento() {
        return Lancamento.builder()
                .ano(2021)
                .mes(1)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    @Test
    public void deveSalvarUmLancamento() {
        Lancamento lancamento = criarLancamento();

        lancamento = repository.save(lancamento);

        Assertions.assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamento lancamento = criarEPersistirLancamento();
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        repository.delete(lancamento);
        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        Assertions.assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamento = criarEPersistirLancamento();
        lancamento.setAno(2020);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        repository.save(lancamento);
        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
        Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
        Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId() {
        Lancamento lancamento = criarEPersistirLancamento();

        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }

    private Lancamento criarEPersistirLancamento() {
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }
}
