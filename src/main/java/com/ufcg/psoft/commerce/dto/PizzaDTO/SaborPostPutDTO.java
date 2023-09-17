package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "O valor não pode estar em branco")
    private double valorMedia;

    @JsonProperty("valorGrande")
    @NotBlank(message = "O valor não pode estar em branco")
    private double valorGrande;

    private DisponibilidadeSabor disponibilidadeSabor;
    @NotBlank(message = "O tipo da pizza não pode estar em branco")
    private TipoDeSabor tipoDeSabor;

    public Long getIdPizza() {
        return this.idPizza;
    }

    public double getValorMedia() {
        return this.valorMedia;
    }

    public double getValorGrande() {
        return this.valorGrande;
    }


    public DisponibilidadeSabor getDisponibilidadeSabor() {
        return disponibilidadeSabor;
    }

    public TipoDeSabor getTipoDeSabor() {
        return tipoDeSabor;
    }

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
