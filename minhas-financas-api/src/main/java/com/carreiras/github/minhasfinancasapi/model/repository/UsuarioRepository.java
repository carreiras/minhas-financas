package com.carreiras.github.minhasfinancasapi.model.repository;

import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
