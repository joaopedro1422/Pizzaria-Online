package com.ufcg.psoft.commerce.model.Estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cardapio.Cardapio;
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
@Table(name = "Logradouro")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    @Column(name = "estabelecimento_id")
    private long id;

    @JsonProperty("codigoAcesso")
    @Column(name = "codigo_acesso")
    private String codigoAcesso;

    @JsonProperty("nome")
    @Column(name = "nome_estabelecimento")
    private String nome;

    @JsonProperty("cardapios")
    @Column(name = "cardapio")
    @OneToMany(cascade=CascadeType.PERSIST)
    private Set<Cardapio> cardapios;

    @JsonProperty("clientes")
    @Column(name = "cliente")
    @OneToMany(cascade=CascadeType.PERSIST)
    private Set<Cliente> clientes;


    @JsonProperty("entregadores")
    @Column(name = "entregador")
    @OneToMany(cascade=CascadeType.PERSIST)
    private Set<Entregador> entregadores;


    //Getters e Setters


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Cardapio> getCardapios() {
        return cardapios;
    }

    public void setCardapios(Set<Cardapio> cardapios) {
        this.cardapios = cardapios;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Set<Entregador> getEntregadores() {
        return entregadores;
    }

    public void setEntregadores(Set<Entregador> entregadores) {
        this.entregadores = entregadores;
    }
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

    public Cardapio cardapioPorCodigoAcesso(String codigoAcessoCardapio, String codigoAcessoEstabelecimento){
        Cardapio cardapio = null;

        if(this.codigoAcesso.equals(codigoAcessoEstabelecimento)){

            for (Cardapio c : cardapios) {

                if(c.getCodigoAcesso().equals(codigoAcessoCardapio)){

                    cardapio = c;
                    break;

                }

            }

        }

        return cardapio;

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

    public SaborPizza CriarSaborPizza(String codigoEstabelecimento){
        SaborPizza saborPizza = null;

        if(codigoEstabelecimento.equals(this.codigoAcesso)){

            //Implementar

        }

        return saborPizza;

    }

}
