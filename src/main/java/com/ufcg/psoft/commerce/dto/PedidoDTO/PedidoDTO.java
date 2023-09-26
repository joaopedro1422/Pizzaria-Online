package com.ufcg.psoft.commerce.dto.PedidoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.validators.CodigoAcessoConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @CodigoAcessoConstraint
    @JsonProperty("codigoAcessoCliente")
    @NotBlank(message = "O código de acesso não pode estar vazio")
    private String codigoAcessoCliente;

    @JsonProperty("pizzas")
    private List<Long> pizzaIds;

    @JsonProperty("enderecoEntrega")
    private String enderecoEntrega;

    @JsonProperty("metodoPagamento")
    @NotBlank(message = "O método de pagamento não pode estar em branco")
    private String metodoPagamento;

}

