package com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pedidos")
public class Pedido {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "pk_id_pedido")
    private long id;

    @JsonProperty("codigoAcesso")
    @Column(nullable = false, name = "desc_codigoAcesso_pedido")
    private String codigoAcesso;

    @JsonProperty("cliente")
    @ManyToOne
    @JoinColumn(name = "fk_id_cliente")
    private Cliente cliente;

    @JsonProperty("estabelecimento")
    @ManyToOne
    @JoinColumn(name = "fk_id_estabelecimento")
    private Estabelecimento estabelecimento;

    @JsonProperty("entregador")
    @ManyToOne
    @JoinColumn(name = "fk_id_entregador")
    private Entregador entregador;

    @JsonProperty("pizzas")
    @OneToMany(mappedBy = "pedido")
    private List<Pizza> pizzas;

    @JsonProperty("enderecoEntrega")
    @Column(name = "desc_endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("metodoPagamento")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "enum_metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    @JsonProperty("valorTotal")
    @Column(nullable = false, name = "num_valor_total")
    private double valorTotal;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "enum_status_pedido")
    private StatusPedido status;

}


