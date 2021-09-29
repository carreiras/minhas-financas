package com.carreiras.github.minhasfinancasapi.service.impl;

import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import com.carreiras.github.minhasfinancasapi.model.repository.UsuarioRepository;
import com.carreiras.github.minhasfinancasapi.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validarEmail(String email) {
        boolean existsUsuarioByEmail = repository.existsByEmail(email);
        if (existsUsuarioByEmail)
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email");
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }
}
