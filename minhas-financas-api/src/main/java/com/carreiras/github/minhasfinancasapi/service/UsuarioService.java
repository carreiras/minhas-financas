package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    void validarEmail(String email);

    Usuario salvar(Usuario usuario);

    Usuario autenticar(String email, String senha);

    Optional<Usuario> obterPorId(Long id);
}
