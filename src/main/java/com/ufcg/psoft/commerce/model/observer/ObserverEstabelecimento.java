package com.ufcg.psoft.commerce.model.observer;


import com.ufcg.psoft.commerce.model.Pedido.Pedido;

public interface ObserverEstabelecimento {

    void update(Pedido pedido);

}