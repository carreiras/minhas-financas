package com.carreiras.github.minhasfinancasapi.api.controller;


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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDto lancamentoDto) {
        try {
            Lancamento lancamento = converter(lancamentoDto);
            lancamento = lancamentoService.salvar(lancamento);
            return new ResponseEntity(lancamento, HttpStatus.CREATED);
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
                        Lancamento lancamento = converter(lancamentoDto);
                        lancamento = lancamentoService.atualizar(lancamento);
                        return ResponseEntity.ok(lancamento);
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
