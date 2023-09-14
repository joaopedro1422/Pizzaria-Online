package com.ufcg.psoft.commerce.exception.Pizza;

public class SaborPizzaNaoEncontradoException extends Exception{

    private long idPizza;

    public SaborPizzaNaoEncontradoException(Long idPizza){
        this.idPizza = idPizza;
    }

    public long getIdPizza() {
        return idPizza;
    }
}
