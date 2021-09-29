package com.carreiras.github.minhasfinancasapi.api.controller;

import com.carreiras.github.minhasfinancasapi.api.dto.UsuarioDto;
import com.carreiras.github.minhasfinancasapi.exception.AutenticacaoException;
import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import com.carreiras.github.minhasfinancasapi.service.LancamentoService;
import com.carreiras.github.minhasfinancasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.getNome())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha())
                .build();
        try {
            return new ResponseEntity(usuarioService.salvar(usuario), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDto usuarioDto) {
        try {
            return ResponseEntity.ok(usuarioService.autenticar(usuarioDto.getEmail(), usuarioDto.getSenha()));
        } catch (AutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.obterPorId(id);
        if (usuario.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }

}
