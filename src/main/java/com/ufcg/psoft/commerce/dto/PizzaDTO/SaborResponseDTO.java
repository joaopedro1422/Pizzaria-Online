package com.ufcg.psoft.commerce.dto.PizzaDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;

import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private String tipoDeSabor;



}