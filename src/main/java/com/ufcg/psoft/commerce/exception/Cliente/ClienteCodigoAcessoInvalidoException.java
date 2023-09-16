package com.ufcg.psoft.commerce.exception.Cliente;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class ClienteCodigoAcessoInvalidoException extends CommerceException {

    public ClienteCodigoAcessoInvalidoException() {

        super("Codigo Invalido");
    }
}