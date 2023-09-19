package com.ufcg.psoft.commerce.service.Entregador;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Entregador.CodigoAcessoEntregadorException;
import com.ufcg.psoft.commerce.exception.Entregador.EntregadorNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.repository.Entregador.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EntregadorV1Service implements EntregadorService{

    @Autowired
    private EntregadorRepository entregadorRepository;


    @Override
    public Entregador adicionarEntregador(EntregadorPostPutDTO entregadorPostPutDTO){
        String codigoAcesso = entregadorPostPutDTO.getCodigoAcesso();
        if(codigoAcesso==null || codigoAcesso.isEmpty()|| !isValidCodigoAcesso(codigoAcesso)){
            throw new CodigoAcessoEntregadorException();
        }
        else{
            return entregadorRepository.save(Entregador.builder()
                    .nome(entregadorPostPutDTO.getNome())
                    .placaVeiculo(entregadorPostPutDTO.getPlacaVeiculo())
                    .tipoVeiculo(entregadorPostPutDTO.getTipoVeiculo())
                    .corVeiculo(entregadorPostPutDTO.getCorVeiculo())
                    .codigoAcesso(entregadorPostPutDTO.getCodigoAcesso())
                    .aprovado(entregadorPostPutDTO.isAprovado())
                    .build());
        }
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
    public Entregador updateEntregador(Long id, EntregadorPostPutDTO entregadorPostPutDTO){
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

        return entregador;
    }

    private boolean isValidCodigoAcesso(String codigoAcesso) {
        return codigoAcesso.matches("[0-9]+") && codigoAcesso.length() == 6;
    }
}
