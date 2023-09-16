package com.ufcg.psoft.commerce.service.Cliente;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.CodigoAcessoEstabelecimentoException;
import com.ufcg.psoft.commerce.exception.Estabelecimento.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ClienteV1Service implements ClienteService {

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private EstabelecimentoV1Service estabelecimentoService;

    @Override
    public Cliente adicionarCliente(ClienteDTO clienteDTO) {
        if (clienteDTO.getCodigoAcesso() == null || clienteDTO.getCodigoAcesso().isEmpty()) {
            throw new ClienteCodigoAcessoInvalidoException();
        } else {
            return clienteRepository.save(
                    Cliente.builder()
                            .nome(clienteDTO.getNome())
                            .endereco(clienteDTO.getEndereco())
                            .codigoAcesso(clienteDTO.getCodigoAcesso())
                            .build()
            );

        }
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
//            ClienteDTO cliente = getCliente(id);
//            validarCodigoAcesso(cliente.getId(), codigoAcesso);
//
//            clienteRepository.deleteById(cliente.getId());
        }

        @Override
        public void atualizarCliente(Long id, ClienteDTO clienteDTO, String codigoAcesso){
//                throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException {
//
//            ClienteDTO cliente = getCliente(id);
//            validarCodigoAcesso(id, codigoAcesso);
//            Cliente atualizacao = getClienteById(cliente.getId());
//
//            atualizacao.setEndereco(clienteDTO.getEndereco());
//            if (clienteDTO.getCodigoAcesso() != null) {
//                atualizacao.setCodigoAcesso(clienteDTO.getCodigoAcesso());
//            }
//            atualizacao.setNome(clienteDTO.getNome());
//            clienteRepository.save(atualizacao);
        }


        @Override
        public List<ClienteDTO> getClientes(String codigoAcesso) throws EstabelecimentoNaoEncontradoException, CodigoAcessoEstabelecimentoException {
            estabelecimentoService.validaCodigoAcessoEstabelecimento(codigoAcesso);
            Stream<Cliente> clientes = StreamSupport.stream(clienteRepository.findAll().spliterator(), false);
            return clientes.map(cliente -> modelMapper.map(cliente, ClienteDTO.class)).toList();
        }



}
