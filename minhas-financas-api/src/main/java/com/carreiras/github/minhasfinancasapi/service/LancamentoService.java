package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    void validar(Lancamento lancamento);
}
