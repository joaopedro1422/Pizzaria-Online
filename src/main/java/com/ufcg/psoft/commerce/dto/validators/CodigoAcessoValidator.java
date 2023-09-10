package com.ufcg.psoft.commerce.dto.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CodigoAcessoValidator implements ConstraintValidator<CodigoAcessoConstraint, String> {

    @Override
    public void initialize(CodigoAcessoConstraint contactNumber) {

    }

    /*
    Verifica se o valor passado é uma String com seis números sequenciais.
    ATENÇÃO: considera valores nulos como válidos, para os casos em que é
    aceitável não passar código de acesso no corpo da requisição (exemplo:
    atualização). Combine com a anotação @NotBlank se quiser alterar este
    comportamento.
     */
    @Override
    public boolean isValid(String codigoAcesso,
                           ConstraintValidatorContext cxt) {
        return codigoAcesso == null || (codigoAcesso.matches("[0-9]+")
                && (codigoAcesso.length() == 6));
    }
}