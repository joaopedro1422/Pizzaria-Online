package com.ufcg.psoft.commerce.exception.Cliente;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class ClienteNaoEncontradoException extends CommerceException {

    public ClienteNaoEncontradoException(){

        super("Cliente não Encontrado");
    }

}