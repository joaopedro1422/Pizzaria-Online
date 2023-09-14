package com.ufcg.psoft.commerce.service.Pizza;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaExistenteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaNaoEncontradoException;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaborV1Service implements SaborService {
    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public SaborPostPutDTO criarSabor(SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException, SaborPizzaExistenteException {
        if(isSaborPizzaCadastrado(sabor.getIdPizza())){
            throw new SaborPizzaExistenteException();
        }
        SaborPizza saborPizza = new SaborPizza(sabor.getSaborPizza(),sabor.getTipoDeSabor(),sabor.getValorMedia(),sabor.getValorGrande(),sabor.getDisponibilidadeSabor());
        saborRepository.save(saborPizza);
        return modelMapper.map(saborPizza,SaborPostPutDTO.class);
    }

    @Override
    public SaborPostPutDTO atualizarSaborPizza(long idPizza, SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException {
        SaborPizza saborPizza = consultarSaborPizzaById(idPizza);

        saborPizza.setSaborDaPizza(sabor.getSaborPizza());
        saborPizza.setTipoDeSabor(sabor.getTipoDeSabor());
        saborPizza.setValorMedia(sabor.getValorMedia());
        saborPizza.setValorGrande(sabor.getValorGrande());
        saborPizza.setDisponibilidade(sabor.getDisponibilidadeSabor());

        salvarSaborPizzaCadastrado(saborPizza);

        return modelMapper.map(saborPizza,SaborPostPutDTO.class);
    }

    @Override
    public SaborPostPutDTO consultarSaborPizza(long idPizza) throws SaborPizzaNaoEncontradoException {
        SaborPizza saborPizza = consultarSaborPizzaById(idPizza);
        return modelMapper.map(saborPizza,SaborPostPutDTO.class);

    }
    @Override
    public List<SaborPostPutDTO> listarSaboresPizza() {
        return saborRepository.findAll()
                .stream()
                .map(saborPizza -> modelMapper.map(saborPizza,SaborPostPutDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SaborPostPutDTO> listarSaboresPizza(TipoDeSabor tipoDeSabor) {
        return saborRepository.findAll()
                .stream()
                .filter(saborPizza -> saborPizza.getTipoDeSabor() == tipoDeSabor)
                .map(saborPizza -> modelMapper.map(saborPizza, SaborPostPutDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletarSaborPizza(long idPizza) throws SaborPizzaNaoEncontradoException {
            SaborPizza saborPizza = consultarSaborPizzaById(idPizza);
            saborRepository.delete(saborPizza);
    }


    // metodos auxiliares
    @Override
    public SaborPizza consultarSaborPizzaById(Long idPizza) throws SaborPizzaNaoEncontradoException {
        return saborRepository.findById(idPizza)
                .orElseThrow(() -> new SaborPizzaNaoEncontradoException(idPizza));
    }

    private boolean isSaborPizzaCadastrado(Long idPizza) throws SaborPizzaNaoEncontradoException {
        try {
            consultarSaborPizza(idPizza);
            return true;
        } catch (SaborPizzaNaoEncontradoException e) {
            return false;
        }
    }

   @Override
    public void salvarSaborPizzaCadastrado(SaborPizza saborPizza) {
        saborRepository.save(saborPizza);
    }




}
