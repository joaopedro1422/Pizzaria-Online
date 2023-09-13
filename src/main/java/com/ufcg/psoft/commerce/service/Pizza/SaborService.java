package com.ufcg.psoft.commerce.service.Pizza;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;

public interface SaborService {
    SaborPostPutDTO criarSabor(SaborPostPutDTO sabor);

    SaborPostPutDTO atualizarSaborPizza(long saborPizzaId, SaborPostPutDTO sabor); // fazer o throws

    SaborPostPutDTO consultarSaborPizza(long saborPizzaId);

    SaborPizza consultarSaborPizzaById(Long saborPizzaId);
}
