package com.ufcg.psoft.commerce.service.Pizza;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoEstabelecimentoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaExistenteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaborV1Service implements SaborService {
    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;


    @Autowired
    private ModelMapper modelMapper;
    @Override
    public SaborResponseDTO criarSabor(Long idEstabelecimento, String codigoAcessoEstabelecimento,SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException, SaborPizzaExistenteException, EstabelecimentoNaoEncontradoException, CodigoAcessoEstabelecimentoException {
        Optional<Estabelecimento> estabelecimentoOptinial = estabelecimentoRepository.findById(idEstabelecimento);
        if(!estabelecimentoOptinial.isPresent()){
            throw new EstabelecimentoNaoEncontradoException();
        }
        Estabelecimento estabelecimento = estabelecimentoOptinial.get();
        if(!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)){
            throw new IllegalArgumentException();
        }

        SaborPizza saborPizza = new SaborPizza(sabor.getSaborPizza(),sabor.getTipoDeSabor(),sabor.getValorMedia(),sabor.getValorGrande(),sabor.getDisponibilidadeSabor());
        saborRepository.save(saborPizza);
        return modelMapper.map(saborPizza, SaborResponseDTO.class);
    }

    @Override
    public SaborResponseDTO atualizarSaborPizza(Long idEstabelecimento, String codigoAcessoEstabelecimento,long idPizza, SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException, EstabelecimentoNaoEncontradoException, CodigoAcessoEstabelecimentoException {
        if(!estabelecimentoRepository.findById(idEstabelecimento).get().getCodigoAcesso().equals(codigoAcessoEstabelecimento)){
            throw new IllegalArgumentException();
        }
        if(!estabelecimentoRepository.findById(idEstabelecimento).isPresent()){
            throw new EstabelecimentoNaoEncontradoException();
        }

        if(saborRepository.findById(idPizza).isPresent()){
            SaborPizza newSabor = modelMapper.map(sabor,SaborPizza.class);
            newSabor = saborRepository.save(newSabor);
            return SaborResponseDTO.builder()
                    .idPizza(newSabor.getIdPizza())
                    .saborDaPizza(newSabor.getSaborDaPizza())
                    .tipoDeSabor(newSabor.getTipoDeSabor())
                    .valorMedia(newSabor.getValorMedia())
                    .valorGrande(newSabor.getValorGrande())
                    .disponibilidadeSabor(newSabor.getDisponibilidadeSabor())
                    .build();
        } else{
            throw new SaborPizzaNaoEncontradoException();
        }

    }

    @Override
    public List<SaborResponseDTO> buscarTodosSaboresPizza(Long idEstabelecimento, String codigoAcessoEstabelecimento) throws CodigoAcessoEstabelecimentoException, EstabelecimentoNaoEncontradoException {
        if(!estabelecimentoRepository.findById(idEstabelecimento).get().getCodigoAcesso().equals(codigoAcessoEstabelecimento)){
            throw new CodigoAcessoEstabelecimentoException();
        }
        if(!estabelecimentoRepository.findById(idEstabelecimento).isPresent()){
            throw new EstabelecimentoNaoEncontradoException();
        }
        List<SaborPizza> todosSabores = saborRepository.findAll();
        List<SaborResponseDTO> saboresResponse = new ArrayList<SaborResponseDTO>();

        for(int j = 0;j<todosSabores.size();j++){
            if(todosSabores.get(j).getEstabelecimento().getId().equals(idEstabelecimento)){
                saboresResponse.add(
                        SaborResponseDTO.builder()
                                .idPizza(todosSabores.get(j).getIdPizza())
                                .saborDaPizza(todosSabores.get(j).getSaborDaPizza())
                                .tipoDeSabor(todosSabores.get(j).getTipoDeSabor())
                                .valorMedia(todosSabores.get(j).getValorMedia())
                                .valorGrande(todosSabores.get(j).getValorGrande())
                                .disponibilidadeSabor(todosSabores.get(j).getDisponibilidadeSabor())
                                .build()
                );
            }
        }
        return saboresResponse;
    }


    @Override
    public void deletarSaborPizza(Long idEstabelecimento, String codigoAcessoEstabelecimento,long idPizza) throws SaborPizzaNaoEncontradoException, CodigoAcessoEstabelecimentoException, EstabelecimentoNaoEncontradoException {

        Optional<Estabelecimento> estabelecimentoOptinial = estabelecimentoRepository.findById(idEstabelecimento);
        if(!estabelecimentoOptinial.isPresent()){
            throw new EstabelecimentoNaoEncontradoException();
        }
        Estabelecimento estabelecimento = estabelecimentoOptinial.get();
        if(!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)){
            throw new CodigoAcessoEstabelecimentoException();
        }
        if(saborRepository.findById(idPizza).isPresent()){
            saborRepository.deleteById(idPizza);
        } else{
            throw new SaborPizzaNaoEncontradoException();
        }
    }

    @Override
    public SaborResponseDTO buscarId(Long idEstabelecimento, String codigoAcessoEstabelecimento, long idPizza) throws EstabelecimentoNaoEncontradoException {
        Optional<Estabelecimento> estabelecimentoOptinial = estabelecimentoRepository.findById(idEstabelecimento);
        if(!estabelecimentoOptinial.isPresent()){
            throw new EstabelecimentoNaoEncontradoException();
        }
        Estabelecimento estabelecimento = estabelecimentoOptinial.get();
        if(!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)){
            throw new IllegalArgumentException();
        }
        if(saborRepository.findById(idPizza).isPresent()){
            SaborPizza saborPizza = saborRepository.findById(idPizza).get();
            return SaborResponseDTO.builder()
                    .idPizza(saborPizza.getIdPizza())
                    .saborDaPizza(saborPizza.getSaborDaPizza())
                    .tipoDeSabor(saborPizza.getTipoDeSabor())
                    .valorMedia(saborPizza.getValorMedia())
                    .valorGrande(saborPizza.getValorGrande())
                    .disponibilidadeSabor(saborPizza.getDisponibilidadeSabor())
                    .build();
        } else{
            throw new SaborPizzaNaoEncontradoException();
        }

    }

}
