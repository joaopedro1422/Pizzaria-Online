package com.ufcg.psoft.commerce.controller.Estabelecimento;


import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cardapio.Cardapio;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/estabelecimentos",  produces = MediaType.APPLICATION_JSON_VALUE)
public class EstabelecimentoV1Controller {

    @Autowired
    private EstabelecimentoV1Service estabelecimentov1Service;

    final String msgErro = "Nenhum estabelecimento encontrado com esse id";

    @PostMapping
    public ResponseEntity<?> post (@RequestBody EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){

        Estabelecimento estabelecimento = estabelecimentov1Service.add(estabelecimentoPostPutRequestDTO);

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
    public ResponseEntity<?> put (@PathVariable("id") long id, @RequestBody EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){
        Estabelecimento estabelecimento = estabelecimentov1Service.put(id, estabelecimentoPostPutRequestDTO);
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
            estabelecimentov1Service.delete(id);
            response = ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }

        return response;



    }

    @RequestMapping(value = "consultaCliente/{codigoAcessoEstabelecimento}/{CodigoAcessoCliente}", method = RequestMethod.GET)
    public ResponseEntity<?> consultaCliente(@PathVariable("codigoAcessoEstabelecimento") String codigoAcessoEstabelecimento,
                                             @PathVariable("CodigoAcessoCliente") String codigoAcessoCliente){


        Estabelecimento estabelecimento = estabelecimentov1Service.getByCodigoAcesso(codigoAcessoEstabelecimento);

        ResponseEntity<?> response;

        if(estabelecimento != null){

            Cliente cliente = estabelecimento.clientePorCodigoAcesso(codigoAcessoCliente, codigoAcessoEstabelecimento);

            if(cliente != null){

                response = ResponseEntity
                        .status(HttpStatus.OK)
                        .body(cliente);

            }else{

                response = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Codigo de acesso de cliente, ou estabelecimento, errado!");

            }

        }else{


            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(msgErro);

        }

        return response;

    }


    @RequestMapping(value = "consultaEntregador/{codigoAcessoEstabelecimento}/{codigoAcessoEntregador}", method = RequestMethod.GET)
    public ResponseEntity<?> consultaEntregador(@PathVariable("codigoAcessoEstabelecimento") String codigoAcessoEstabelecimento,
                                                @PathVariable("codigoAcessoEntregador") String codigoAcessoEntregador){

        Estabelecimento estabelecimento = estabelecimentov1Service.getByCodigoAcesso(codigoAcessoEstabelecimento);

        ResponseEntity<?> response;

        if(estabelecimento != null){
            Entregador entregador = estabelecimento.entregadorPorCodigoAcesso(codigoAcessoEntregador, codigoAcessoEstabelecimento);

            if(entregador != null){

                response = ResponseEntity
                        .status(HttpStatus.OK)
                        .body(entregador);

            }else{

                response = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Codigo de acesso do entregador, ou estabelecimento, incorreto!");

            }



        }else{

            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(msgErro);

        }

        return response;


    }

}
