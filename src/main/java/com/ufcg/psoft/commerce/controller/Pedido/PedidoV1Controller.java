package com.ufcg.psoft.commerce.controller.Pedido;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoNaoCancelavelException;
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
            @RequestParam("clienteCodigoAcesso") String clienteCodigoAcesso,
            @RequestBody @Valid PedidoDTO pedidoDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.criarPedido(clienteCodigoAcesso, pedidoDTO));
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
            @RequestParam("metodoPagamento") MetodoPagamento metodoPagamento,
            @RequestParam("clienteCodigoAcesso") String clienteCodigoAcesso
    ) throws PedidoNaoEncontradoException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.confirmarPagamento(id, metodoPagamento, clienteCodigoAcesso));
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable("id") Long id,
            @RequestParam("clienteCodigoAcesso") String clienteCodigoAcesso
    ) {
        try {
            pedidoService.cancelarPedido(id, clienteCodigoAcesso);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (PedidoNaoEncontradoException | PedidoCodigoAcessoIncorretoException | PedidoNaoCancelavelException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    @ExceptionHandler(PedidoNaoEncontradoException.class)
    ResponseEntity<?> handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
