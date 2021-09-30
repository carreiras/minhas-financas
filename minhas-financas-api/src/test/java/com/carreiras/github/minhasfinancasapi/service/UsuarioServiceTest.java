package com.carreiras.github.minhasfinancasapi.service;

import com.carreiras.github.minhasfinancasapi.exception.AutenticacaoException;
import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import com.carreiras.github.minhasfinancasapi.model.repository.UsuarioRepository;
import com.carreiras.github.minhasfinancasapi.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ActiveProfiles("teste")
@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl usuarioService;

    @MockBean
    UsuarioRepository usuarioRepository;

    private static Usuario criarUsuario() {
        return Usuario
                .builder()
                .id(1l)
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }

    @Test
    public void deveSalvarUmUsuario() {
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString()); // método mockado por spy
        Usuario usuario = criarUsuario();
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.salvar(new Usuario());

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test
    public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
        Usuario usuario = criarUsuario();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail("usuario@email.com");

        usuarioService.salvar(new Usuario());

        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        Usuario usuario = criarUsuario();
        Mockito.when(usuarioRepository.findByEmail("usuario@email.com")).thenReturn(Optional.of(usuario));

        Usuario usuarioAutenticado = usuarioService.autenticar("usuario@email.com", "senha");

        Assertions.assertThat(usuarioAutenticado).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable erroAutenticacao = Assertions.catchThrowable(() -> usuarioService.autenticar("usuario@email.com", "senha"));

        Assertions.assertThat(erroAutenticacao).isInstanceOf(AutenticacaoException.class).hasMessage("Usuário não encontrado para o e-mail informado.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        String senha = "senha";
        Usuario usuario = criarUsuario();
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable erroAutenticacao = Assertions.catchThrowable(() -> usuarioService.autenticar("usuario@email.com", "123456"));

        Assertions.assertThat(erroAutenticacao).isInstanceOf(AutenticacaoException.class).hasMessage("Senha inválida");
    }

    @Test()
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Throwable erroValidacaoEmail = Assertions.catchThrowable(() -> usuarioService.validarEmail("usuario@email.com"));

        Assertions.assertThat(erroValidacaoEmail).isInstanceOf(RegraNegocioException.class).hasMessage("Já existe um usuário cadastrado com este email");
    }

    @Test
    public void deveValidarEmail() {
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        usuarioService.validarEmail("email@email.com");
    }
}
