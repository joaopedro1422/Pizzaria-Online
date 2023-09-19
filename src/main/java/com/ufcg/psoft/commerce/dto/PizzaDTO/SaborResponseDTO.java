package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborResponseDTO {

    @JsonProperty("idPizza")
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    private double valorMedia;

    @JsonProperty("valorGrande")
    private double valorGrande;

    @JsonProperty("disponibilidadeSabor")
    private Boolean disponibilidadeSabor;

    @JsonProperty("tipoDeSabor")
    private TipoDeSabor tipoDeSabor;

}