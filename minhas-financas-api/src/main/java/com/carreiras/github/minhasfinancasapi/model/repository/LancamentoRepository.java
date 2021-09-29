package com.carreiras.github.minhasfinancasapi.model.repository;

import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<LancamentoRepository, Long> {
}
