package com.ufcg.psoft.commerce.service.Pedido;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido criarPedido(String clienteCodigoAcesso, PedidoDTO pedido);

    Pedido atualizarPedido(Long pedidoId, String clienteCodigoAcesso, PedidoDTO pedidoDTO);

    void removerPedido(Long id);

    Pedido getPedido(Long id);

    List<Pedido> getPedidos();

    void confirmarPagamento(Long id, String metodoPagamento, String codigoAcesso);

    boolean validarCodigoAcesso(Long id, String codigoAcesso) throws PedidoCodigoAcessoIncorretoException, PedidoNaoEncontradoException;


}
