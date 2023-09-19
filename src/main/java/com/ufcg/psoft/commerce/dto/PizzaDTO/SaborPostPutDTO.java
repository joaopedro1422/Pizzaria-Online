package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SaborPostPutDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    @NotBlank(message = "O sabor da pizza não pode estar em branco")
//    @NotNull(message = "O sabor da pizza não pode estar nulo")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    @NotNull(message = "O valor não pode estar em branco")
    @Positive(message = "PrecoM deve ser maior que zero")
    private Double valorMedia;

    @JsonProperty("valorGrande")
    @NotNull(message = "O valor não pode estar em branco")
    @Positive(message = "PrecoG deve ser maior que zero")
    private Double valorGrande;

    @JsonProperty("disponibilidadeSabor")
    @NotNull(message = "Disponibilidade obrigatoria")
    private Boolean disponibilidadeSabor;

    @JsonProperty("tipoDeSabor")
    @NotNull(message = "Disponibilidade obrigatoria")
    private TipoDeSabor tipoDeSabor;
}