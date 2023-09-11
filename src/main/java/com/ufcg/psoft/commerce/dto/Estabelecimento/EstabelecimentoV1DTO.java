package com.ufcg.psoft.commerce.dto.Estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cardapio.Cardapio;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoV1DTO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("clientes")
    private Set<Cliente> clientes;

    @JsonProperty("cardapios")
    private Set<Cardapio> cardapios;

    @JsonProperty("entregadores")
    private Set<Entregador> entreagadores;


}
