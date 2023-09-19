package com.ufcg.psoft.commerce.model.SaborPizza;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "saboresPizza")
public class SaborPizza {

    @JsonProperty("idPizza")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_sabor")
    private Long idPizza;

    @JsonProperty("saborDaPizza")
    @Column(nullable = false, name = "desc_saborDaPizza")
    private String saborDaPizza;

    @JsonProperty("valorMedia")
    @Column(name = "double_valor_media", nullable = false)
    private double valorMedia;

    @JsonProperty("valorGrande")
    @Column(name = "double_valor_grande", nullable = false)
    private double valorGrande;

    private DisponibilidadeSabor disponibilidadeSabor;
    private TipoDeSabor tipoDeSabor;

    public boolean isDisponivel(){
        return this.disponibilidadeSabor.toString().equals("DISPONIVEL");
    }


    @OneToMany
    private List<Cliente> observers;

    {
        observers = new ArrayList<>();
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "estabelecimento_pk_id")
    private Estabelecimento estabelecimento;

    public SaborPizza(String saborDaPizza, TipoDeSabor tipoDeSabor, double valorMedia, double valorGrande, DisponibilidadeSabor disponibilidadeSabor) {
        this.saborDaPizza = saborDaPizza;
        this.tipoDeSabor = tipoDeSabor;
        this.valorMedia = valorMedia;
        this.valorGrande = valorGrande;
        this.disponibilidadeSabor = disponibilidadeSabor;
    }


}
