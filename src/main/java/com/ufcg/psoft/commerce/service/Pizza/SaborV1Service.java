package com.ufcg.psoft.commerce.service.Pizza;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborV1Service implements SaborService {
    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public SaborPostPutDTO criarSabor(SaborPostPutDTO sabor) {
        // fazer validacao
        SaborPizza saborPizza = new SaborPizza(sabor.getSaborPizza(),sabor.getTipoDeSabor(),sabor.getValorMedia(),sabor.getValorGrande(),sabor.getDisponibilidadeSabor());
        saborRepository.save(saborPizza);
        return modelMapper.map(saborPizza,SaborPostPutDTO.class);
    }

    @Override
    public SaborPostPutDTO atualizarSaborPizza(long saborPizzaId, SaborPostPutDTO sabor) {
        SaborPizza saborPizza = consultarSaborPizzaById(saborPizzaId);

        saborPizza.setSaborDaPizza(sabor.getSaborPizza());
        //saborPizza.setTipoDeSabor(sabor.getTipoDeSabor()); // falta esse get
        saborPizza.setValorMedia(sabor.getValorMedia());
        saborPizza.setValorGrande(sabor.getValorGrande());
        //saborPizza.setDisponibilidade(sabor.getDisponibilidadeSabor()); // falta esse get

        // salvar o sabor

        return modelMapper.map(saborPizza,SaborPostPutDTO.class);
    }

    @Override
    public SaborPostPutDTO consultarSaborPizza(long saborPizzaId) {
        SaborPizza saborPizza = consultarSaborPizzaById(saborPizzaId);
        return modelMapper.map(saborPizza,SaborPostPutDTO.class);

    }

    @Override
    public SaborPizza consultarSaborPizzaById(Long saborPizzaId) {
        return saborRepository.findById(saborPizzaId)
                .orElseThrow(() -> new IllegalArgumentException());
        // alterar o thorrw para a excessao de sabor nao encontrado
    }

}
