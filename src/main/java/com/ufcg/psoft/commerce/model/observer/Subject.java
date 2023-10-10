package com.ufcg.psoft.commerce.model.observer;

import com.ufcg.psoft.commerce.model.Cliente.Cliente;

public interface Subject {


    public void register(Cliente observer);

    public void unregister(Cliente observer);

    public void notifyObservers() throws CloneNotSupportedException;


}