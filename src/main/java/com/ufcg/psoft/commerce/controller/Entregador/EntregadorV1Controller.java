package com.ufcg.psoft.commerce.controller.Entregador;
/*
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutDTO;
import com.ufcg.psoft.commerce.service.Entregador.EntregadorService;
import com.ufcg.psoft.commerce.service.Entregador.EntregadorV1Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/entregadores", produces = MediaType.APPLICATION_JSON_VALUE)
public class EntregadorV1Controller {

    @Autowired
    EntregadorV1Service entregadorService;

    @PostMapping
    ResponseEntity<?> criarEntregador(
            @RequestBody @Valid EntregadorPostPutDTO entregadorPostPutDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entregadorService.adicionarEntregador(entregadorPostPutDTO));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> buscarEntregadorPorId(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.getEntregador(id));
    }

    @GetMapping
    ResponseEntity<?> listarEntregadores(
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.getEntregadores());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> atualizarEntregador(
            @PathVariable("id") Long id,
            @RequestBody @Valid EntregadorPostPutDTO entregadorPostPutDTO
    ){
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.updateEntregador(id, entregadorPostPutDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateStatus(
            @Valid @RequestParam("codigoAcessoEstabelecimento")String codigoAcessoEstabelecimento,
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody EntregadorPostPutDTO entregadorPostPutDTO
    ){
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.updateStatus(id));
    }


}
*/