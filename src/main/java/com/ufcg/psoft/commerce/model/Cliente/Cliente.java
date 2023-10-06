package com.ufcg.psoft.commerce.model.Cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
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
    @Column(nullable = false,name = "pk_id_cliente")
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

    @OneToOne
    private SaborPizza subject = null;

    public boolean isSubscribed(SaborPizza subject){
        if(this.subject != null){
            return this.subject.getSaborDaPizza().equals(subject.getSaborDaPizza());
        }
        return false;
    }

    public void subscribeTo(SaborPizza subject) {
        if(!isSubscribed(subject)){
            subject.register(this);
            this.subject = subject;

        }
    }
    public void unsubscribeFrom(SaborPizza subject) {
        this.subject.unregister(this);
        this.subject = null;
    }

}
