package com.ufcg.psoft.commerce.service.Cliente;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteV1Service implements ClienteService {

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private ModelMapper modelMapper;

        @Override
        public ClienteDTO adicionarCliente(ClienteDTO clienteDTO) {
            Cliente cliente = new Cliente(clienteDTO.getNome(), clienteDTO.getEndereco(), clienteDTO.getCodigoAcesso());
            clienteRepository.save(cliente);
            return modelMapper.map(cliente, ClienteDTO.class);
        }

        public ClienteDTO getCliente(Long id) throws ClienteNaoEncontradoException {
            Cliente cliente = getClienteById(id);
            return modelMapper.map(cliente, ClienteDTO.class);
        }

        @Override
        public boolean validarCodigoAcesso(Long id, String codigoAcesso) throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException {
            Cliente cliente = getClienteById(id);
            if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
                throw new ClienteCodigoAcessoIncorretoException();
            }
            return true;
        }

        private Cliente getClienteById(Long id) throws ClienteNaoEncontradoException {
            return clienteRepository.findById(id).orElseThrow(ClienteNaoEncontradoException::new);
        }

        @Override
        public void removerCliente(Long id, String codigoAcesso) throws ClienteNaoEncontradoException, ClienteCodigoAcessoIncorretoException {
            ClienteDTO cliente = getCliente(id);
            validarCodigoAcesso(cliente.getId(), codigoAcesso);

            clienteRepository.deleteById(cliente.getId());
        }

        @Override
        public void atualizarCliente(Long id, ClienteDTO clienteDTO, String codigoAcesso)
                throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException {

            ClienteDTO cliente = getCliente(id);
            validarCodigoAcesso(id, codigoAcesso);
            Cliente atualizacao = getClienteById(cliente.getId());

            atualizacao.setEndereco(clienteDTO.getEndereco());
            if (clienteDTO.getCodigoAcesso() != null) {
                atualizacao.setCodigoAcesso(clienteDTO.getCodigoAcesso());
            }
            atualizacao.setNome(clienteDTO.getNome());
            clienteRepository.save(atualizacao);
        }


    }
