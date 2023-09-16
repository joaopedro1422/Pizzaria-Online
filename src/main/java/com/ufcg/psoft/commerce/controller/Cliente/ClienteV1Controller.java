package com.ufcg.psoft.commerce.controller.Cliente;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.service.Cliente.ClienteV1Service;
import com.ufcg.psoft.commerce.util.ErroCliente;
import com.ufcg.psoft.commerce.util.ErroValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteV1Controller {

    @Autowired
    ClienteV1Service clienteV1Service;

    @PostMapping
    ResponseEntity<?> criarCliente(
            @RequestBody @Valid ClienteDTO clienteDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteV1Service.adicionarCliente(clienteDTO));
    }
//    @PutMapping("/{clienteId}/{codigoAcesso}")
//    ResponseEntity<?> atualizarCliente(@PathVariable Long clienteId,
//                                       @PathVariable String codigoAcesso,
//                                       @Valid @RequestBody ClienteDTO cliente) {
//        try {
//            clienteV1Service.atualizarCliente(clienteId, cliente, codigoAcesso);
//            return ResponseEntity.status(HttpStatus.OK).build();
//        } catch (ClienteCodigoAcessoIncorretoException e) {
//            return ErroCliente.erroCodigoAcessoIncorreto();
//        } catch (ClienteNaoEncontradoException e) {
//            return criarCliente(cliente);
//        }
//    }

    @GetMapping("/{idCliente}")
    ResponseEntity<?> getCliente(@PathVariable long idCliente) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clienteV1Service.getCliente(idCliente));
        } catch (ClienteNaoEncontradoException e) {
            return ErroCliente.erroClienteNaoEncontrado(idCliente);
        }
    }

    @DeleteMapping("/{clienteId}/{codigoAcesso}")
    ResponseEntity<?> removerCliente(@PathVariable Long clienteId, @PathVariable String codigoAcesso) {
        try {
            clienteV1Service.removerCliente(clienteId, codigoAcesso);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ClienteCodigoAcessoIncorretoException e) {
            return ErroCliente.erroCodigoAcessoIncorreto();
        } catch (ClienteNaoEncontradoException e) {
            return ErroCliente.erroClienteNaoEncontrado(clienteId);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ErroValidacao.erroFalhaValidacao(ex.getFieldErrors());
    }

}

