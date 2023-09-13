package com.ufcg.psoft.commerce.service.Cliente;


import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;

public interface ClienteService {
    ClienteDTO adicionarCliente(ClienteDTO cliente);
    ClienteDTO getCliente(Long id) throws ClienteNaoEncontradoException;
    boolean validarCodigoAcesso(Long id, String codigoAcesso) throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException;
    void removerCliente(Long id, String codigoAcesso) throws ClienteNaoEncontradoException, ClienteCodigoAcessoIncorretoException;
    void atualizarCliente(Long id, ClienteDTO cliente, String codigoAcesso) throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException;
}
