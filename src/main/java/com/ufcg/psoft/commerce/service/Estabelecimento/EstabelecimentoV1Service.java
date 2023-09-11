package com.ufcg.psoft.commerce.service.Estabelecimento;

import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoV1Repository;
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

        return estabelecimentoV1Repository.save(converteTDOParaEntidade(estabelecimentoV1DTO));

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

}
