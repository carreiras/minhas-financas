package com.carreiras.github.minhasfinancasapi.api.dto;

import com.carreiras.github.minhasfinancasapi.model.enums.StatusLancamento;
import com.carreiras.github.minhasfinancasapi.model.enums.TipoLancamento;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LancamentoDto {

    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Long usuario;
    private TipoLancamento tipo;
    private StatusLancamento status;
}
