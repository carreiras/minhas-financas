package com.carreiras.github.minhasfinancasapi.api.controller;


import com.carreiras.github.minhasfinancasapi.api.dto.AtualizaStatusDto;
import com.carreiras.github.minhasfinancasapi.api.dto.LancamentoDto;
import com.carreiras.github.minhasfinancasapi.exception.RegraNegocioException;
import com.carreiras.github.minhasfinancasapi.model.entity.Lancamento;
import com.carreiras.github.minhasfinancasapi.model.entity.Usuario;
import com.carreiras.github.minhasfinancasapi.model.enums.StatusLancamento;
import com.carreiras.github.minhasfinancasapi.model.enums.TipoLancamento;
import com.carreiras.github.minhasfinancasapi.service.LancamentoService;
import com.carreiras.github.minhasfinancasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDto lancamentoDto) {
        try {
            return new ResponseEntity(lancamentoService.salvar(converter(lancamentoDto)), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto lancamentoDto) {
        return lancamentoService.obterPorId(id)
                .map(entity -> {
                    try {
                        lancamentoDto.setId(entity.getId());
                        return ResponseEntity.ok(lancamentoService.atualizar(converter(lancamentoDto)));
                    } catch (RegraNegocioException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return lancamentoService.obterPorId(id)
                .map(entity -> {
                    lancamentoService.deletar(entity);
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping()
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
    ) {
        Lancamento lancamento = Lancamento.builder()
                .descricao(descricao)
                .mes(mes)
                .ano(ano)
                .build();
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Consulta não realizada. Usuário não encontrado para o Id informado.");
        }
        lancamento.setUsuario(usuario.get());
        List<Lancamento> lancamentos = lancamentoService.buscar(lancamento);
        return ResponseEntity.ok(lancamentos);
    }

    @PutMapping("/{id}/atualizar-status")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDto atualizaStatusDto) {
        return lancamentoService.obterPorId(id)
                .map(entity -> {
                    StatusLancamento status = StatusLancamento.valueOf(atualizaStatusDto.getStatus());
                    if (StatusLancamento.valueOf(atualizaStatusDto.getStatus()) == null)
                        return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento.");
                    try {
                        entity.setStatus(status);
                        return ResponseEntity.ok(lancamentoService.atualizar(entity));
                    } catch (RegraNegocioException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter(LancamentoDto dto) {
        Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
        TipoLancamento tipoLancamento = TipoLancamento.valueOf(dto.getTipo().name());
        StatusLancamento statusLancamento = dto.getStatus() != null ?
                StatusLancamento.valueOf(dto.getStatus().name()) : StatusLancamento.PENDENTE;
        return Lancamento
                .builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .ano(dto.getAno())
                .mes(dto.getMes())
                .valor(dto.getValor())
                .usuario(usuario)
                .tipo(tipoLancamento)
                .status(statusLancamento)
                .build();
    }
}
