package com.ufcg.psoft.commerce.model.Cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clientes")
public class Cliente {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_cliente")
    private long id;

    @JsonProperty("codigoAcesso")
    @Column(nullable = false, name = "desc_codigoAcesso_cliente")
    private String codigoAcesso;

    @JsonProperty("nome")
    @Column(nullable = false, name = "desc_nome_cliente")
    private String nome;

    @JsonProperty("endereco")
    @Column(nullable = false, name = "desc_endereco_cliente")
    private String endereco;

    public Cliente(String nome, String endereco, String codigoAcesso) {

    }
}
