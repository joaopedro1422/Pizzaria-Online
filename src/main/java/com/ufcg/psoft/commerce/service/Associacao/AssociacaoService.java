package com.ufcg.psoft.commerce.service.Associacao;


import com.ufcg.psoft.commerce.model.Associacao.Associacao;
import com.ufcg.psoft.commerce.repository.Associacao.AssociacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoService {

    @Autowired
    AssociacaoRepository associacaoRepository;

    public Associacao criarAssociacao(String idEntregador, String codigoAcessoEntregador,
                                      String idEstabelecimento){

        Associacao associacao = Associacao.builder()
                .entregadorId(Long.parseLong(idEntregador))
                .estabelecimentoId(Long.parseLong(idEstabelecimento))
                .codigoAcesso(codigoAcessoEntregador)
                .build();

        return associacaoRepository.save(associacao);

    }



}
