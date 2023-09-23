package com.ufcg.psoft.commerce.exception.Associacao;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class EntregadorCodigoAcessoNaoEntradoException extends CommerceException {

    public EntregadorCodigoAcessoNaoEntradoException(){

        super("Nenhum entregador encontrado com esse codigo de acesso");

    }

}
