package com.ufcg.psoft.commerce.dto.Entregador;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorPostPutRequestDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo de acesso obrigatorio")
    @NotNull(message = "codigo de acesso obrigatorio")
    private String codigoAcesso;

    @JsonProperty("nome")
    @NotBlank(message = "O nome do entregador não pode estar em branco")
    @NotNull(message = "O nome do entregador não pode ser nulo")
    private String nome;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "A placa do entregador não pode estar em branco")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "O tipo do veiculo do entregador não pode estar em branco")
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")

    private String corVeiculo;

    @JsonProperty("aprovado")

    private boolean aprovado;


}
