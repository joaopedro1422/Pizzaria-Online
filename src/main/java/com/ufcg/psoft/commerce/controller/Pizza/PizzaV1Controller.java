package com.ufcg.psoft.commerce.controller.Pizza;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import com.ufcg.psoft.commerce.service.Pizza.SaborV1Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizza/v1/")
@CrossOrigin
public class PizzaV1Controller {
    
    @Autowired
    SaborV1Service saborV1Service;
    @Autowired
    EstabelecimentoV1Service estabelecimentoV1Service;
    
    @PostMapping(value = "/saboresPizza/{codigoEstabelecimento}")
    public ResponseEntity<?> criarSaborPizza (@PathVariable("codigoEstabelecimento")String codigoEstabelecimento,@Valid @RequestBody SaborPostPutDTO sabor){
        SaborPostPutDTO saborPizza = saborV1Service.criarSabor(sabor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saborPizza);
    }

}
