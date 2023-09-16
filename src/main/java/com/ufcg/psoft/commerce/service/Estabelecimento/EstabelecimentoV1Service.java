package com.ufcg.psoft.commerce.service.Estabelecimento;

import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoEstabelecimentoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoV1Repository;
import com.ufcg.psoft.commerce.util.GerarCodigoAcessoEstabelecimento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EstabelecimentoV1Service {

    @Autowired
    private EstabelecimentoV1Repository estabelecimentoV1Repository;

    @Bean
    private ModelMapper mapeadorEstabelecimento(){

        return new ModelMapper();

    }

    private Estabelecimento converteTDOParaEntidade(EstabelecimentoV1DTO estabelecimentoV1DTO){

        return mapeadorEstabelecimento().map(estabelecimentoV1DTO, Estabelecimento.class);

    }

    public Estabelecimento add(EstabelecimentoV1DTO estabelecimentoV1DTO){
        Estabelecimento estabelecimento = converteTDOParaEntidade(estabelecimentoV1DTO);

        GerarCodigoAcessoEstabelecimento gerador = new GerarCodigoAcessoEstabelecimento(estabelecimentoV1Repository);

        estabelecimento.setCodigoAcesso(gerador.gerar());

        return estabelecimentoV1Repository.save(estabelecimento);

    }

    public Estabelecimento getOne(long id){

        Estabelecimento estabelecimento = null;

        if(estabelecimentoV1Repository.existsById(id)){

            estabelecimento = estabelecimentoV1Repository.findById(id).get();


        }

        return estabelecimento;


    }

    public List<Estabelecimento> getAll(){

        return estabelecimentoV1Repository.findAll();
    }

    public Estabelecimento put (long id, EstabelecimentoV1DTO estabelecimentoV1DTO){
        Estabelecimento estabelecimento = null;

        if(estabelecimentoV1Repository.existsById(id)){

            estabelecimento = converteTDOParaEntidade(estabelecimentoV1DTO);

            estabelecimento.setId(id);

            estabelecimentoV1Repository.saveAndFlush(estabelecimento);

        }


        return estabelecimento;

    }

    public void delete (long id){
        if (estabelecimentoV1Repository.existsById(id)){

            estabelecimentoV1Repository.deleteById(id);

        }


    }

    public Estabelecimento getByCodigoAcesso(String codigoAcesso){

        Estabelecimento estabelecimento = null;

        if(estabelecimentoV1Repository.existsByCodigoAcesso(codigoAcesso)){


            estabelecimento = estabelecimentoV1Repository.findByCodigoAcesso(codigoAcesso).get();

        }

        return estabelecimento;

    }

    public void validaCodigoAcessoEstabelecimento(String codigoAcesso) throws CodigoAcessoEstabelecimentoException, EstabelecimentoNaoEncontradoException {
        Estabelecimento estabelecimento = findEstabelecimento();

        if (!estabelecimento.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoAcessoEstabelecimentoException();
        }
    }

    // Método para verificar se o código de acesso do estabelecimento existe
    private boolean verificaCodigoAcesso(String codigoAcesso) throws EstabelecimentoNaoEncontradoException {
        Estabelecimento estabelecimento = findEstabelecimento();
        return estabelecimento.getCodigoAcesso().equals(codigoAcesso);
    }

    // Método para encontrar o estabelecimento
    public Estabelecimento findEstabelecimento() throws EstabelecimentoNaoEncontradoException {
        List<Estabelecimento> estabelecimentoList = estabelecimentoV1Repository.findAll();
        if (estabelecimentoList.isEmpty()) {
            throw new EstabelecimentoNaoEncontradoException();
        }

        return estabelecimentoList.get(0);
    }

}
