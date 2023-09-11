package com.ufcg.psoft.commerce.controller.Estabelecimento;


import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoV1DTO;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/estabelecimento/v1/",  produces = MediaType.APPLICATION_JSON_VALUE)
public class EstabelecimentoV1Controller {

    @Autowired
    private EstabelecimentoV1Service estabelecimentov1Service;

    final String msgErro = "Nenhum estabelecimento encontrado com esse id";

    @PostMapping
    public ResponseEntity<?> post (@RequestBody EstabelecimentoV1DTO estabelecimentoV1DTO){

        Estabelecimento estabelecimento = estabelecimentov1Service.add(estabelecimentoV1DTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimento);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") long id){
        Estabelecimento estabelecimento = estabelecimentov1Service.getOne(id);
        ResponseEntity<?> response;

        if(estabelecimento == null){

            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(msgErro);

        }else{

            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(estabelecimento);

        }

        return response;

    }


    @GetMapping
    public ResponseEntity<?> getAll(){


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentov1Service.getAll());

    }

    @PutMapping("{id}")
    public ResponseEntity<?> put (@PathVariable("id") long id, @RequestBody EstabelecimentoV1DTO estabelecimentoV1DTO){
        Estabelecimento estabelecimento = estabelecimentov1Service.put(id, estabelecimentoV1DTO);
        ResponseEntity<?> response;

        if(estabelecimento == null){

            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(msgErro);


        }else{

            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(estabelecimento);

        }

        return response;



    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id){
        Estabelecimento estabelecimento = estabelecimentov1Service.getOne(id);
        ResponseEntity<?> response;


        if(estabelecimento == null){

            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(msgErro);

        }else{

            response = ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }

        return response;



    }

}
