package com.ufcg.psoft.commerce.util;

import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoV1Repository;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;

import java.util.Random;

public class GerarCodigoAcessoEstabelecimento {
    private EstabelecimentoV1Repository estabelecimentoV1Service;

    public GerarCodigoAcessoEstabelecimento(EstabelecimentoV1Repository estabelecimentoV1Repository){

        this.estabelecimentoV1Service = estabelecimentoV1Repository;

    }

    public String gerar(){

        Random alea = new Random();

        String codigo = Integer.toString(alea.nextInt(0, 999999));
        for (int i = 0; i < 6 - codigo.length() ; i++) {

            codigo = "0" + codigo;

        }

        if(estabelecimentoV1Service.existsByCodigoAcesso(codigo)){

            codigo = gerar();

        }

        return codigo;

    }

}
