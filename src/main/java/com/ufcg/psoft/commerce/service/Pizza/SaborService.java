package com.ufcg.psoft.commerce.service.Pizza;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaExistenteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaNaoEncontradoException;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;

import java.util.List;

public interface SaborService {
    SaborPostPutDTO criarSabor(SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException, SaborPizzaExistenteException;

    SaborPostPutDTO atualizarSaborPizza(long idPizza, SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException; // fazer o throws

    SaborPostPutDTO consultarSaborPizza(long idPizza) throws SaborPizzaNaoEncontradoException;

    SaborPizza consultarSaborPizzaById(Long idPizza) throws SaborPizzaNaoEncontradoException;

    void salvarSaborPizzaCadastrado(SaborPizza saborPizza) throws SaborPizzaNaoEncontradoException;

    List<SaborPostPutDTO> listarSaboresPizza();

    List<SaborPostPutDTO> listarSaboresPizza(TipoDeSabor tipoDeSabor);

    void deletarSaborPizza(long idPizza) throws SaborPizzaNaoEncontradoException;

}
