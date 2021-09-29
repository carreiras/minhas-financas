package com.carreiras.github.minhasfinancasapi.service.impl;

import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import com.carreiras.github.minhasfinancasapi.model.repository.LancamentoRepository;
import com.carreiras.github.minhasfinancasapi.service.LancamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
        this.repository = lancamentoRepository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(""))
            throw new RegraNegocioException("Informe uma Descrição válida.");
        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12)
            throw new RegraNegocioException("Informe um Mês válido.");
        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4)
            throw new RegraNegocioException("Informe um Ano válido.");
        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null)
            throw new RegraNegocioException("Informe um Usuário.");
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
            throw new RegraNegocioException("Informe um Valor válido.");
        if (lancamento.getTipo() == null)
            throw new RegraNegocioException("Informe um Tipo de Lançamento.");
    }
}
