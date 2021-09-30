package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import com.carreiras.github.minhasfinancasapi.model.enums.StatusLancamento;
import com.carreiras.github.minhasfinancasapi.model.repository.LancamentoRepository;
import com.carreiras.github.minhasfinancasapi.model.repository.LancamentoRepositoryTest;
import com.carreiras.github.minhasfinancasapi.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl lancamentoService;

    @MockBean
    LancamentoRepository lancamentoRepository;

    @Test
    public void deveSalvarUmLancamento() {
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(lancamentoService).validar(lancamentoASalvar);
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        Lancamento lancamento = lancamentoService.salvar(lancamentoASalvar);

        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(lancamentoService).validar(lancamentoASalvar);

        Assertions.catchThrowableOfType(() -> lancamentoService.salvar(lancamentoASalvar), RegraNegocioException.class);
        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.doNothing().when(lancamentoService).validar(lancamentoSalvo);
        Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        lancamentoService.atualizar(lancamentoSalvo);

        Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        Usuario usuario = Usuario.builder().id(1l).email("usuario@email.com").senha("senha").nome("usuario").build();
        lancamento.setUsuario(usuario);

        Assertions.catchThrowableOfType(() -> lancamentoService.atualizar(lancamento), NullPointerException.class);

        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        lancamentoService.deletar(lancamento);

        Mockito.verify(lancamentoRepository).delete(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        Assertions.catchThrowableOfType(() -> lancamentoService.deletar(lancamento), NullPointerException.class);

        Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamentos() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        List<Lancamento> resultado = lancamentoService.buscar(lancamento);

        Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(lancamentoService).atualizar(lancamento);

        lancamentoService.atualizarStatus(lancamento, novoStatus);

        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(lancamentoService).atualizar(lancamento);
    }
}
