package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SaborPostPutDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    @NotBlank(message = "O sabor da pizza não pode estar em branco")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    @NotNull(message = "O valor não pode estar nulo")
    private double valorMedia;

    @JsonProperty("valorGrande")
    @NotNull(message = "O valor não pode estar nulo")
    private double valorGrande;

    private DisponibilidadeSabor disponibilidadeSabor;

    private TipoDeSabor tipoDeSabor;

    public String getSaborPizza() {
        return this.saborDaPizza;
    }

    public void setSaborDaPizza(String saborDaPizza) {
        this.saborDaPizza = saborDaPizza;
    }

    public void setDisponibilidadeSabor(DisponibilidadeSabor disponibilidadeSabor) {
        this.disponibilidadeSabor = disponibilidadeSabor;
    }

    public void setTipoDeSabor(TipoDeSabor tipoDeSabor) {
        this.tipoDeSabor = tipoDeSabor;
    }
}
