package com.ufcg.psoft.commerce.exception.Entregador;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class EntregadorNaoEncontradoException extends CommerceException {
    public EntregadorNaoEncontradoException(){

        super("Entregador não Encontrado");
    }
}
