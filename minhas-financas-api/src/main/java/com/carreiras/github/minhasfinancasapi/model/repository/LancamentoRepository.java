package com.carreiras.github.minhasfinancasapi.model.repository;

import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
