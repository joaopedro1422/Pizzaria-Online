package com.ufcg.psoft.commerce.model.Pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;
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

    @JsonProperty("pizzas")
    @ManyToMany
    @JoinTable(
            name = "pedidos_pizzas",
            joinColumns = @JoinColumn(name = "fk_id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "fk_id_pizza")
    )
    private List<Pizza> pizzas;

    @JsonProperty("enderecoEntrega")
    @Column(name = "desc_endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("codigoAcessoCliente")
    @Column(nullable = false, name = "desc_codigoAcesso_cliente")
    private String codigoAcessoCliente;

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

    @JsonProperty("codigoAcessoEstabelecimento")
    @Column(name = "fk_codigoAcessoEstabelecimento")
    private String codigoAcessoEstabelecimento;

}


