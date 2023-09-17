package com.ufcg.psoft.commerce.controller.Pizza;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoEstabelecimentoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaExistenteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaJaExisteException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaNaoEncontradoException;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
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
       try{
           estabelecimentoV1Service.validaCodigoAcessoEstabelecimento(codigoAcessoEstabelecimento);
           SaborPostPutDTO saborPizza = saborV1Service.criarSabor(sabor);
           return new ResponseEntity<>(saborPizza,HttpStatus.OK);
       } catch (SaborPizzaJaExisteException e) {
           throw new RuntimeException(e);
       } catch (CodigoAcessoEstabelecimentoException | EstabelecimentoNaoEncontradoException e) {
           throw new RuntimeException(e);
       }
    }

    @PutMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{idPizza}")
    public ResponseEntity<?> atualizarSaborPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("idPizza") long idPizza,@Valid @RequestBody SaborPostPutDTO sabor) throws SaborPizzaNaoEncontradoException {
        try{
            estabelecimentoV1Service.validaCodigoAcessoEstabelecimento(codigoAcessoEstabelecimento);
            saborV1Service.atualizarSaborPizza(idPizza,sabor);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (SaborPizzaJaExisteException e) {
            throw new RuntimeException(e);
        } catch (CodigoAcessoEstabelecimentoException | EstabelecimentoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/saboresPizza/{codigoAcessoEstabelecimento}/{idPizza}")
    public ResponseEntity<?> consultarSaboresPizza(@Valid @PathVariable("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,@Valid @PathVariable("idPizza") long idPizza) throws SaborPizzaNaoEncontradoException {

        try{
            estabelecimentoV1Service.validaCodigoAcessoEstabelecimento(codigoAcessoEstabelecimento);
            SaborPostPutDTO sabor = saborV1Service.consultarSaborPizza(idPizza);
            return new ResponseEntity<SaborPostPutDTO>(sabor,HttpStatus.OK);
        } catch (SaborPizzaJaExisteException e) {
            throw new RuntimeException(e);
        } catch (CodigoAcessoEstabelecimentoException | EstabelecimentoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
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
        try{
            estabelecimentoV1Service.validaCodigoAcessoEstabelecimento(codigoAcessoEstabelecimento);
            saborV1Service.deletarSaborPizza(idPizza);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SaborPizzaJaExisteException e) {
            throw new RuntimeException(e);
        } catch (CodigoAcessoEstabelecimentoException | EstabelecimentoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }










}
