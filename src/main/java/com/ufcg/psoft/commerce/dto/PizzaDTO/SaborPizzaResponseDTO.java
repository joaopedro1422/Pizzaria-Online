package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborPizzaResponseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    @NotBlank(message = "O sabor da pizza não pode estar em branco")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    @NotBlank(message = "O valor não pode estar em branco")
    private double valorMedia;

    @JsonProperty("valorGrande")
    @NotBlank(message = "O valor não pode estar em branco")
    private double valorGrande;

    private DisponibilidadeSabor disponibilidadeSabor;
    @NotBlank(message = "O tipo da pizza não pode estar em branco")
    private TipoDeSabor tipoDeSabor;

}
