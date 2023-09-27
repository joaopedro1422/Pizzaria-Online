package com.ufcg.psoft.commerce.exception.Associacao;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;

public class EstabelecimentoIdNaoEncontradoException extends CommerceException {

    public EstabelecimentoIdNaoEncontradoException(){

        super("O estabelecimento consultado nao existe!");

    }

}
