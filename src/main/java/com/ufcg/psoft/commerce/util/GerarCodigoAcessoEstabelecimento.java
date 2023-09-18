package com.ufcg.psoft.commerce.util;

import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;

import java.util.Random;

public class GerarCodigoAcessoEstabelecimento {
    private EstabelecimentoRepository estabelecimentoV1Service;

    public GerarCodigoAcessoEstabelecimento(EstabelecimentoRepository estabelecimentoRepository){

        this.estabelecimentoV1Service = estabelecimentoRepository;

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
