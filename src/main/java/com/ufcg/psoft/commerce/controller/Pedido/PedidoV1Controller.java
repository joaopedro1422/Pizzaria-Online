package com.ufcg.psoft.commerce.controller.Pedido;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.service.Pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoV1Controller {

    @Autowired
    PedidoService pedidoService;

    @PostMapping
    ResponseEntity<Pedido> criarPedido(
            @RequestParam String clienteCodigoAcesso,
            @RequestBody @Valid PedidoDTO pedidoDTO
    ) {
        Pedido pedido = pedidoService.criarPedido(clienteCodigoAcesso, pedidoDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedido);
    }

    @PutMapping("/{id}")
    ResponseEntity<Pedido> atualizarPedido(
            @PathVariable("id") Long id,
            @RequestParam String clienteCodigoAcesso,
            @RequestBody @Valid PedidoDTO pedidoDTO
    ) throws PedidoNaoEncontradoException {
        Pedido pedido = pedidoService.atualizarPedido(id, clienteCodigoAcesso, pedidoDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedido);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> excluirPedido(
            @PathVariable("id") Long id
    ) throws PedidoNaoEncontradoException {
        pedidoService.removerPedido(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Pedido> buscarPedidoPorId(
            @PathVariable("id") Long id
    ) throws PedidoNaoEncontradoException {
        Pedido pedido = pedidoService.getPedido(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedido);
    }

    @GetMapping
    ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.getPedidos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidos);
    }

    @PutMapping("/{id}/confirmar-pagamento")
    ResponseEntity<?> confirmarPagamento(
            @PathVariable("id") Long id,
            @RequestParam("metodoPagamento") String metodoPagamento,
            @RequestParam("codigoAcesso") String codigoAcesso
    ) throws PedidoNaoEncontradoException {
        pedidoService.confirmarPagamento(id, metodoPagamento, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    ResponseEntity<?> handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
