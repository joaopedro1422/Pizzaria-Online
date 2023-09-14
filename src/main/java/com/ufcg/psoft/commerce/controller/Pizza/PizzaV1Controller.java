package com.ufcg.psoft.commerce.controller.Pizza;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaExistenteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaNaoEncontradoException;
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

import java.util.List;

@RestController
@RequestMapping("/pizza/v1/")
@CrossOrigin
public class PizzaV1Controller {
    
    @Autowired
    SaborV1Service saborV1Service;
    @Autowired
    EstabelecimentoV1Service estabelecimentoV1Service;
    
    @PostMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}")
    public ResponseEntity<?> criarSaborPizza (@PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @RequestBody SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException, SaborPizzaExistenteException {
        // fazer validacao se sabor ja existe,estabelecimento nao existe,codigo de acesso invalido
         sabor = saborV1Service.criarSabor(sabor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sabor);
    }

    @PutMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{idPizza}")
    public ResponseEntity<?> atualizarSaborPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("idPizza") long idPizza,@Valid @RequestBody SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException {
        // fazer validacao do codigo de acesso,sabor,estabelecimento nao encontrado
            saborV1Service.atualizarSaborPizza(idPizza,sabor);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{idPizza}")
    public ResponseEntity<?> consultarSaboresPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("idPizza") long idPizza) throws SaborPizzaNaoEncontradoException {
        SaborPostPutDTO sabor = saborV1Service.consultarSaborPizza(idPizza);
        return new ResponseEntity<SaborPostPutDTO>(sabor,HttpStatus.OK);
        // fazer validacao
    }

    @GetMapping(value = "/saboresPizza")
    public ResponseEntity<?> consultarCardapio(){
        List<SaborPostPutDTO> cardapio = saborV1Service.listarSaboresPizza();
        if(cardapio.isEmpty()){
            throw new NullPointerException();
        }
        return new ResponseEntity<List<SaborPostPutDTO>>(cardapio,HttpStatus.OK);
    }

    @GetMapping(value = "/saboresPizza/salgados")
    public ResponseEntity<?> consultarCardapioSaboresSalgados(){
        List<SaborPostPutDTO> cardapio = saborV1Service.listarSaboresPizza(TipoDeSabor.SALGADO);
        if(cardapio.isEmpty()){
            throw new NullPointerException(); // erroCardapio
        }
        return new ResponseEntity<List<SaborPostPutDTO>>(cardapio,HttpStatus.OK);
    }

    @GetMapping(value = "/saboresPizza/doces")
    public ResponseEntity<?> consultarCardapioSaboresDoces(){
        List<SaborPostPutDTO> cardapio = saborV1Service.listarSaboresPizza(TipoDeSabor.DOCE);
        if(cardapio.isEmpty()){
            throw new NullPointerException();
        }
        return new ResponseEntity<List<SaborPostPutDTO>>(cardapio,HttpStatus.OK);
    }

    @DeleteMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{idPizza}")
    public ResponseEntity<?> deletarSaborPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("idPizza") long idPizza) throws SaborPizzaNaoEncontradoException {
        saborV1Service.deletarSaborPizza(idPizza);
        return new ResponseEntity<>(HttpStatus.OK);
        // fazer validacao codigo estabelecimento, sabor n encoontrado, estabelecimento nao encontrado, codigo de acesso incorreto
    }










}
