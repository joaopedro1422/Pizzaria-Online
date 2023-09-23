package com.ufcg.psoft.commerce.exception.Associacao;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class EntregadorIdNaoEncontradoException extends CommerceException {

    public EntregadorIdNaoEncontradoException(){

        super("Nenhum Entregador encontrado com esse id!");

    }

}
