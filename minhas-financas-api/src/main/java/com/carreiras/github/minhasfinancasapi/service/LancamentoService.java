package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;

import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    void validar(Lancamento lancamento);

    Optional<Lancamento> obterPorId(Long id);
}
