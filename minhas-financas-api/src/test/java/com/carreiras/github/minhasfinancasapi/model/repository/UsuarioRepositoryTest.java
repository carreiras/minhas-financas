package com.carreiras.github.minhasfinancasapi.model.repository;

import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private static Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        Usuario usuario = criarUsuario();
        testEntityManager.persist(usuario);

        boolean usuarioEncontrado = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(usuarioEncontrado).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        boolean usuarioEncontrado = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(usuarioEncontrado).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        Usuario usuario = criarUsuario();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail() {
        Usuario usuario = criarUsuario();
        testEntityManager.persist(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(usuarioEncontrado.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase() {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(usuarioEncontrado.isPresent()).isFalse();
    }
}
