package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
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

    @JsonProperty(value = "idPizza")
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    private double valorMedia;

    @JsonProperty("valorGrande")
    private double valorGrande;

    @JsonProperty("disponibilidadeSabor")
    private DisponibilidadeSabor disponibilidadeSabor;

    @JsonProperty("tipoDeSabor")
    private TipoDeSabor tipoDeSabor;

}
