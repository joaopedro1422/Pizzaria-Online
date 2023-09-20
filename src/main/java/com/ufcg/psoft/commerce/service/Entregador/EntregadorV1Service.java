package com.ufcg.psoft.commerce.service.Entregador;

import com.ufcg.psoft.commerce.dto.Entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.Entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Entregador.CodigoAcessoEntregadorException;
import com.ufcg.psoft.commerce.exception.Entregador.EntregadorNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.repository.Entregador.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregadorV1Service implements EntregadorService{

    @Autowired
    private EntregadorRepository entregadorRepository;


    @Bean
    private ModelMapper mapeadorEntregador(){

        return new ModelMapper();
    }

    private Entregador converteTDOParaEntidade(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO){

        return mapeadorEntregador().map(entregadorPostPutRequestDTO, Entregador.class);

    }
    @Override
    public Entregador adicionarEntregador(EntregadorPostPutRequestDTO entregadorPostPutDTO){

        Entregador entregador = converteTDOParaEntidade(entregadorPostPutDTO);
        if(entregador.getCodigoAcesso().length() != 6){

            throw new CodigoAcessoInvalidoException();
        }
        return entregadorRepository.save(entregador);
    }

    @Override
    public Entregador getEntregador(Long id) {
        Optional<Entregador> entregadorOptional = entregadorRepository.findById(id);
        if (entregadorOptional.isPresent()) {
            return entregadorOptional.get();
        } else {
            throw new EntregadorNaoEncontradoException();
        }
    }

    @Override
    public List<Entregador> getEntregadores(){
        return entregadorRepository.findAll();
    }

    @Override
    public Entregador updateEntregador(Long id, EntregadorPostPutRequestDTO entregadorPostPutDTO){
        Optional<Entregador> entregadorOptional = entregadorRepository.findById(id);

        if(entregadorOptional.isPresent()){
            String codigoAcesso = entregadorPostPutDTO.getCodigoAcesso();
            if (codigoAcesso != null && !codigoAcesso.isEmpty() && !isValidCodigoAcesso(codigoAcesso)) {
                throw new EntregadorNaoEncontradoException();
            }
            Entregador entregador= entregadorOptional.get();
            entregador.setCorVeiculo(entregadorPostPutDTO.getCorVeiculo());
            entregador.setPlacaVeiculo(entregadorPostPutDTO.getPlacaVeiculo());
            entregador.setTipoVeiculo(entregadorPostPutDTO.getTipoVeiculo());
            return entregadorRepository.save(entregador);
        } else {
            throw new EntregadorNaoEncontradoException();
        }
    }


    @Override
    public Entregador updateStatus(Long id){
        Optional<Entregador> entregadorOptional = entregadorRepository.findById(id);
        Entregador entregador= entregadorOptional.get();
        entregador.setAprovado(true);
//
        return entregador;
    }

    private boolean isValidCodigoAcesso(String codigoAcesso) {
        return codigoAcesso.matches("[0-9]+") && codigoAcesso.length() == 6;
    }
}
