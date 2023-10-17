package com.ufcg.psoft.commerce.dto.PedidoDTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.validators.CodigoAcessoConstraint;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {


    @CodigoAcessoConstraint
    @JsonProperty("codigoAcesso")
    @NotBlank(message = "O código de acesso não pode estar vazio")
    private String codigoAcesso;

    @JsonProperty("clienteId")
    @NotNull(message = "O ID do cliente não pode estar vazio")
    private Long clienteId;

    @JsonProperty("estabelecimentoId")
    @NotNull(message = "O ID do estabelecimento não pode estar vazio")
    private Long estabelecimentoId;

    @JsonProperty("pizzas")
    private List<Pizza> pizzas;

    @JsonProperty("enderecoEntrega")
    private String enderecoEntrega;

    @JsonProperty("metodoPagamento")
    @NotBlank(message = "O método de pagamento não pode estar em branco/Inválido")
    private String metodoPagamento;

    @JsonProperty("status")
    private StatusPedido statusPedido;


}

