package com.ufcg.psoft.commerce.model.Entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name= "entregadores")
public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "pk_id_entregador")
    private Long id;


    @JsonProperty("codigoAcesso")
    @Column(nullable = false, name = "codigoAcesso_entregador")
    private String codigoAcesso;

    @JsonProperty("nome")
    @Column(nullable = false, name = "nome_entregador")
    private String nome;

    @JsonProperty("placaVeiculo")
    @Column(nullable = false, name = "placaVeiculo_entregador")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @Column(nullable = false, name = "tipoVeiculo_entregador")
    private TipoVeiculo tipoVeiculo;

    @JsonProperty("corVeiculo")
    @Column(nullable = false, name = "corVeiculo_entregador")
    private String corVeiculo;

    @JsonProperty("aprovado")
    @Column(nullable = false, name = "desc_aprovaçap_entregador")
    private boolean aprovado;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoAcesso() {
        return codigoAcesso;
    }

    public void setCodigoAcesso(String codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getCorVeiculo() {
        return corVeiculo;
    }

    public void setCorVeiculo(String corVeiculo) {
        this.corVeiculo = corVeiculo;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
}