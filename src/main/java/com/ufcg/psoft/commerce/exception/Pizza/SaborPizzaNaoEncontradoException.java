package com.ufcg.psoft.commerce.exception.Pizza;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class SaborPizzaNaoEncontradoException extends CommerceException {

    private long idPizza;

    public SaborPizzaNaoEncontradoException(Long idPizza){
        this.idPizza = idPizza;
    }

    public SaborPizzaNaoEncontradoException() {

    }

    public long getIdPizza() {
        return idPizza;
    }
}
