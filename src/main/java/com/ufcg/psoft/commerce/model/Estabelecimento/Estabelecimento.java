package com.ufcg.psoft.commerce.model.Estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Estabelecimento")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "estabelecimento_id")
    private Long id;

    @JsonProperty("codigoAcesso")
    @Column(name = "codigo_acesso")
    private String codigoAcesso;

    @JsonProperty("nome")
    @Column(name = "nome_estabelecimento")
    private String nome;

    @OneToMany(mappedBy = "estabelecimento",cascade = {CascadeType.REMOVE,CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<SaborPizza> saboresPizza;

    @OneToMany(cascade=CascadeType.PERSIST)
    private Set<Cliente> clientes;


    @OneToMany(cascade=CascadeType.PERSIST)
    private Set<Entregador> entregadores;


    //Funcoes de Estabelecimento, toda operação feita em estabelecimento tem que ser forneciado o codigo de acesso
    //do estabelecimento

    public Cliente clientePorCodigoAcesso(String codigoAcessoCliente, String codigoAcessoEstabelecimento){
        Cliente cliente = null;

        if (codigoAcessoEstabelecimento.equals(this.codigoAcesso)){

            for(Cliente c : clientes){

                if(c.getCodigoAcesso().equals(codigoAcessoCliente)){

                    cliente = c;
                    break;

                }


            }

        }

        return cliente;

    }


    public Entregador entregadorPorCodigoAcesso(String codigoAcessoEntregador, String codigoAcessoEstabelecimento){
        Entregador entregadorEncontrado = null;

        if(codigoAcessoEstabelecimento.equals(this.codigoAcesso)){


            for (Entregador entregador : this.entregadores){

                if(entregador.getCodigoAcesso().equals(codigoAcessoEntregador)){

                    entregadorEncontrado = entregador;
                    break;
                }
            }
        }
        return entregadorEncontrado;

    }

    public void setDisponibilidadeSabor(Long idSaborPizza, boolean disponibilidade){
        for (SaborPizza sabor: saboresPizza) {
            if(sabor.getIdPizza().equals(idSaborPizza)){
                sabor.setDisponibilidadeSabor(disponibilidade);
            }
        }
    }

    public Entregador associarEntregador(String codigoAcessoEntregador, String codigoAcessoEstabelecimento){
        Entregador entregador = null;

        if(codigoAcessoEstabelecimento.equals(this.codigoAcesso)){

            //Implementar

        }

        return entregador;

    }


    public void aprovarEntregador(String codigoAcessoEstabelecimento, String codigoAcessoEntregador,
                                  boolean aprovado){

        if (codigoAcessoEstabelecimento.equals(this.codigoAcesso)){

            //Implementar

        }

    }


}