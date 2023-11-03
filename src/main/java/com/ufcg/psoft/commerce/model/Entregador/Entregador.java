package com.ufcg.psoft.commerce.model.Entregador;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
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
    private Long id;//


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
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    @Column(nullable = false, name = "corVeiculo_entregador")
    private String corVeiculo;

    @JsonProperty("aprovado")
    @Column(nullable = false, name = "desc_aprovaçap_entregador")
    private boolean aprovado;

    @JsonProperty("isDisponibilidade")
    @Column(nullable = false, name = "desc_disponibilidade_entregador")
    private boolean isDisponibilidade;

    @JsonProperty("EstadodeDisponibilidade")
    @Column(name = "desc_estado_diponibilidade")
    private String estadoDeDisposicao;

}