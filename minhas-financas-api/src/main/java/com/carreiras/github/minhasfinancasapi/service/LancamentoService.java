package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import com.carreiras.github.minhasfinancasapi.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar(Lancamento lancamento);

    void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);

    void validar(Lancamento lancamento);

    Optional<Lancamento> obterPorId(Long id);
}
