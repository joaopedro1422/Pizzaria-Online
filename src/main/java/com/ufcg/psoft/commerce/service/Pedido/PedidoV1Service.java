package com.ufcg.psoft.commerce.service.Pedido;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoV1Service implements PedidoService{

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        String codigoAcesso = pedidoDTO.getCodigoAcesso();

        if (codigoAcesso == null || codigoAcesso.isEmpty() || !isValidCodigoAcesso(codigoAcesso)) {
            throw new ClienteCodigoAcessoInvalidoException();
        } else {
            return clienteRepository.save(
                    Cliente.builder()
                            .nome(clienteDTO.getNome())
                            .endereco(clienteDTO.getEndereco())
                            .codigoAcesso(codigoAcesso)
                            .build()
            );
        }
    }

    @Override
    public Pedido atualizarPedido(Long id, PedidoDTO pedido) {
        return null;
    }

    @Override
    public void removerPedido(Long id) {

    }

    @Override
    public Pedido getPedido(Long id) {
        return null;
    }

    @Override
    public List<Pedido> getPedidos() {
        return null;
    }

    @Override
    public void confirmarPagamento(Long id, String metodoPagamento, String codigoAcesso) {

    }

    @Override
    public boolean validarCodigoAcesso(Long id, String codigoAcesso) throws PedidoCodigoAcessoIncorretoException, PedidoNaoEncontradoException {
        return false;
    }
}
