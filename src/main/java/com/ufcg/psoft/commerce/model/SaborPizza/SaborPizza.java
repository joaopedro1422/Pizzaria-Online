package com.ufcg.psoft.commerce.model.SaborPizza;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
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

    private double valorMedia;

    private double valorGrande;

    private DisponibilidadeSabor disponibilidadeSabor;
    private TipoDeSabor tipoDeSabor;


    public SaborPizza(String saborDaPizza, TipoDeSabor tipoDeSabor, double valorMedia, double valorGrande, DisponibilidadeSabor disponibilidadeSabor) {
        this.saborDaPizza = saborDaPizza;
        this.tipoDeSabor = tipoDeSabor;
        this.valorMedia = valorMedia;
        this.valorGrande = valorGrande;
        this.disponibilidadeSabor = disponibilidadeSabor;
    }

    public boolean isDisponivel(){
        return this.disponibilidadeSabor.toString().equals("DISPONIVEL");
    }

    public TipoDeSabor getTipoDeSabor() {
        return this.tipoDeSabor;
    }

    public DisponibilidadeSabor getDisponibilidadeSabor() {return this.disponibilidadeSabor;}

    /*public void setDisponibilidade(DisponibilidadeSabor disponibilidadeSabor) {
        this.disponibilidadeSabor = disponibilidadeSabor;
        if(disponibilidadeSabor== DisponibilidadeSabor.DISPONIVEL){
            this.notifyObservers();
        }
    }*/

    @OneToMany
    private List<Cliente> observers;

    {
        observers = new ArrayList<Cliente>();
    }
    public List<Cliente> getObservers() {
        return observers;
    }


    public Long getIdPizza() {
        return idPizza;
    }

    public String getSaborDaPizza() {
        return saborDaPizza;
    }

    public double getValorMedia() {
        return valorMedia;
    }

    public void setValorMedia(double valorMedia) {
        this.valorMedia = valorMedia;
    }

    public double getValorGrande() {
        return valorGrande;
    }

    public void setValorGrande(double valorGrande) {
        this.valorGrande = valorGrande;
    }

    public void setTipoDeSabor(TipoDeSabor tipoDeSabor) {
        this.tipoDeSabor = tipoDeSabor;
    }

    public void setSaborDaPizza(String saborPizza) {
        this.saborDaPizza = saborDaPizza;
    }

    public void setDisponibilidade(DisponibilidadeSabor disponibilidadeSabor) {
        this.disponibilidadeSabor = disponibilidadeSabor;
    }
    /*public void notifyObservers() {
        for(Cliente c : observers) {
            c.update(this);
        }
    }*/

}
