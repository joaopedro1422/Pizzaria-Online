package com.ufcg.psoft.commerce.model.observer;

import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;

public interface SubjectPedido {

    public void register(Estabelecimento observer);

    public void notifyObservers() throws CloneNotSupportedException;


}