package com.ufcg.psoft.commerce.service.Associacao;


import com.ufcg.psoft.commerce.exception.Associacao.EntregadorCodigoAcessoNaoEntradoException;
import com.ufcg.psoft.commerce.exception.Associacao.EntregadorIdNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Associacao.EstabelecimentoIdNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Associacao.Associacao;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.repository.Associacao.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.Entregador.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoService {

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    public Associacao criarAssociacao(String idEntregador, String codigoAcessoEntregador,
                                      String idEstabelecimento){

        Long idNumericoEntregador = Long.parseLong(idEntregador);
        Long idNumericoEstabelecimento = Long.parseLong(idEstabelecimento);


        if (!estabelecimentoRepository.existsById(idNumericoEstabelecimento)){

            throw new EstabelecimentoIdNaoEncontradoException();

        }else if (!entregadorRepository.existsById(idNumericoEntregador)){

            throw new EntregadorIdNaoEncontradoException();

        }else if (!entregadorRepository.existsByCodigoAcesso(codigoAcessoEntregador)){

            throw new EntregadorCodigoAcessoNaoEntradoException();

        }

        Associacao associacao = Associacao.builder()
                .entregadorId(idNumericoEntregador)
                .estabelecimentoId(idNumericoEstabelecimento)
                .codigoAcesso(codigoAcessoEntregador)
                .build();

        return associacaoRepository.save(associacao);

    }

    public Associacao aprovarEntregador(String idEntregador, String codigoAcesso, String idEstabelecimento){

        Long idNumericoEntregador = Long.parseLong(idEntregador);
        Long idNumericoEstatabelecimento = Long.parseLong(idEstabelecimento);

        Associacao resultado;

        if(!associacaoRepository.existsByEstabelecimentoId(idNumericoEstatabelecimento)){

            throw new EstabelecimentoIdNaoEncontradoException();

        }else if (!associacaoRepository.existsByEntregadorId(idNumericoEntregador)){

            throw new EntregadorIdNaoEncontradoException();

        }else if (!estabelecimentoRepository.existsByCodigoAcesso(codigoAcesso)){

            throw new EntregadorCodigoAcessoNaoEntradoException();

        }else{

            Estabelecimento estabelecimento = estabelecimentoRepository.findById(idNumericoEstatabelecimento).get();

            if (!estabelecimento.getCodigoAcesso().equals(codigoAcesso)) throw new EntregadorCodigoAcessoNaoEntradoException();


        }

        resultado = associacaoRepository.findByEntregadorIdAndEstabelecimentoId(

                idNumericoEntregador,
                idNumericoEstatabelecimento

        ).get();

        resultado.setStatus(true);

        return associacaoRepository.saveAndFlush(resultado);

    }



}
