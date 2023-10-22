package com.ufcg.psoft.commerce.model.Cliente;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "saborPizza_pk_id")
    private SaborPizza subject = null; // instância dessa classe pode estar associada a uma única instância de SaborPizza

   /* verifica se o objeto atual (representado pela instância da classe que contém esse método)
   está inscrito (ou seja, observando) uma instância específica de SaborPizza.
   Ele faz isso verificando se a variável subject não é nula e se o sabor da pizza associada
   ao subject atual é igual ao sabor da pizza passado como argumento
    */
    public boolean isSubscribed(SaborPizza subject){
        if(this.subject != null){
            return this.subject.getSaborDaPizza().equals(subject.getSaborDaPizza());
        }
        return false;
    }

   /*permite que a instância atual se inscreva (ou seja, observe) uma instância específica de SaborPizza.
   Ele faz isso chamando o método register no subject passado como argumento
   (esse método não está presente no código que você forneceu) e atribuindo o subject passado como argumento
   à variável subject desta instância
    */
    public void subscribeTo(SaborPizza subject) {
        if(!isSubscribed(subject) && subject.getDisponibilidadeSabor()==false){
            subject.register(this);
            this.subject = subject;

        }
    }

    /* instância atual cancele a inscrição (ou seja, deixe de observar)
    a instância específica de SaborPizza que está associada a ela.
    Isso é feito chamando o método unregister no subject
    (esse método também não está presente no código que você forneceu)
    e atribuindo null à variável subject.
    */
    public void unsubscribeFrom(SaborPizza subject) {
        if (this.subject != null) {
            this.subject.unregister(this);
            this.subject = null;
        }
    }
    public String notificaPedidoEmRota(Entregador entregador){
        String saida = "";
        saida = ("Nome do entregador responsável pela entrega: "+ entregador.getNome()
                +"/nPlaca do veículo de"+ entregador.getNome()+": " + entregador.getPlacaVeiculo());
        return saida;
    }

    public void update(SaborPizza saborPizza) {
        if(this.subject == null) {
            System.out.println(this.nome + " você não está inscrito no sabor " + saborPizza.getSaborDaPizza());
            return;
        }
        System.out.println(this.nome + ", a pizza de " + saborPizza.getSaborDaPizza() + " agora está disponivel" );
    }

}
