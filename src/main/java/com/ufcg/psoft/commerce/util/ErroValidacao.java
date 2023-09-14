package com.ufcg.psoft.commerce.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErroValidacao {
    public static ResponseEntity<CustomErrorType> erroFalhaValidacao(List<FieldError> errors) {

        StringBuilder builder = new StringBuilder();

        errors.forEach(erro -> {
            builder.append(String.format("Erro no parâmetro %s: %s", erro.getField(), erro.getDefaultMessage()));
                }
        );
        return new ResponseEntity<>(new CustomErrorType(builder.toString()), HttpStatus.BAD_REQUEST);
    }
}