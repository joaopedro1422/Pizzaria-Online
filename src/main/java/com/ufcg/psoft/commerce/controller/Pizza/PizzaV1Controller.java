package com.ufcg.psoft.commerce.controller.Pizza;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import com.ufcg.psoft.commerce.service.Pizza.SaborService;
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
    
    @PostMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}")
    public ResponseEntity<?> criarSaborPizza (@PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @RequestBody SaborPostPutDTO sabor){
        // fazer validacao se sabor ja existe,estabelecimento nao existe,codigo de acesso invalido
         sabor = saborV1Service.criarSabor(sabor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sabor);
    }

    @PutMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{saborPizzaId}")
    public ResponseEntity<?> atualizarSaborPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("saborPizzaId") long saborPizzaId,@Valid @RequestBody SaborPostPutDTO sabor){
        // fazer validacao do codigo de acesso,sabor,estabelecimento nao encontrado
            saborV1Service.atualizarSaborPizza(saborPizzaId,sabor);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{saborPizzaId}")
    public ResponseEntity<?> consultarSaboresPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("saborPizzaId") long saborPizzaId){
        SaborPostPutDTO sabor = saborV1Service.consultarSaborPizza(saborPizzaId);
        return new ResponseEntity<SaborPostPutDTO>(sabor,HttpStatus.OK);
        // fazer validacao
    }




}
