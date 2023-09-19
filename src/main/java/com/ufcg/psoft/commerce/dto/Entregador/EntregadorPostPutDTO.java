package com.ufcg.psoft.commerce.dto.EntregadorDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoVeiculo;
import com.ufcg.psoft.commerce.model.Cardapio.Cardapio;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorPostPutDTO {

    @JsonProperty("codigoAcesso")
    private String codigoAcesso;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("placaVeiculo")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    private TipoVeiculo tipoVeiculo;

    @JsonProperty("corVeiculo")
    private String corVeiculo;

    @JsonProperty("aprovado")
    private boolean aprovado;
}
