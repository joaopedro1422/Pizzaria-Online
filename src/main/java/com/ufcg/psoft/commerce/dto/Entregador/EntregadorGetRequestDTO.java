package com.ufcg.psoft.commerce.dto.Entregador;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorGetRequestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("codigoAcesso")
    private String codigoAcesso;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("placaVeiculo")//
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    private String corVeiculo;

    @JsonProperty("aprovado")
    private boolean aprovado;


}
