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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Cliente(String nome, String endereco, String codigoAcesso) {

    }
}
