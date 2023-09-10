package com.ufcg.psoft.commerce.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroCliente {
    static final String CODIGO_ACESSO_INVALIDO = "O código de acesso informado precisa conter seis números!";

    static final String CODIGO_ACESSO_INCORRETO = "O código de acesso informado está incorreto!";

    static final String CLIENTE_NAO_ENCONTRADO = "O cliente com o id %s não foi encontrado no sistema!";

    public static ResponseEntity<?> erroCodigoAcessoInvalido() {
        return new ResponseEntity<>(new CustomErrorType(ErroCliente.CODIGO_ACESSO_INVALIDO), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static ResponseEntity<?> erroCodigoAcessoIncorreto() {
        return new ResponseEntity<>(new CustomErrorType(ErroCliente.CODIGO_ACESSO_INCORRETO), HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<?> erroClienteNaoEncontrado(Long id) {
        return new ResponseEntity<>(new CustomErrorType(String.format(ErroCliente.CLIENTE_NAO_ENCONTRADO, id)), HttpStatus.NOT_FOUND);
    }
}
