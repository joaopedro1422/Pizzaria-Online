package com.ufcg.psoft.commerce.service.Cliente;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaClienteCadastrado;
import com.ufcg.psoft.commerce.exception.Pizza.SaborPizzaEstaDisponivel;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.service.Estabelecimento.EstabelecimentoV1Service;
import com.ufcg.psoft.commerce.service.Pizza.SaborService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
public class ClienteV1Service implements ClienteService {

    @Autowired
    private  ClienteRepository clienteRepository;

    @Autowired
    private SaborService saborService;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EstabelecimentoV1Service estabelecimentoService;

    @Override
    public Cliente adicionarCliente(ClienteDTO clienteDTO) {
        String codigoAcesso = clienteDTO.getCodigoAcesso();

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
    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if (clienteOptional.isPresent()) {
            String codigoAcesso = clienteDTO.getCodigoAcesso();
            if (codigoAcesso != null && !codigoAcesso.isEmpty() && !isValidCodigoAcesso(codigoAcesso)) {
                throw new ClienteCodigoAcessoIncorretoException();
            }

            Cliente cliente = clienteOptional.get();
            cliente.setNome(clienteDTO.getNome());
            cliente.setEndereco(clienteDTO.getEndereco());

            return clienteRepository.save(cliente);
        } else {
            throw new ClienteNaoEncontradoException();
        }
    }

    @Override
    public void removerCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException();
        }
        // Não há necessidade de validar o código de acesso para remover o cliente.
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente getCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            return clienteOptional.get();
        } else {
            throw new ClienteNaoEncontradoException();
        }
    }

    @Override
    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public boolean validarCodigoAcesso(Long id, String codigoAcesso) throws ClienteCodigoAcessoIncorretoException, ClienteNaoEncontradoException {
        Cliente cliente = getClienteById(id);
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new ClienteCodigoAcessoIncorretoException();
        }
        return true;
    }

    @Override
    public void demonstrarInteressePizza(Long id, String codigoAcesso, Long idPizza) {
        Cliente cliente = getClienteById(id);
        if (codigoAcesso != null && !codigoAcesso.isEmpty() && !isValidCodigoAcesso(codigoAcesso)) {
            throw new ClienteCodigoAcessoIncorretoException();
        }
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new ClienteCodigoAcessoIncorretoException();
        }
        SaborPizza pizza = saborService.consultarSaborPizzaById(idPizza);
        if (!String.valueOf(pizza.getDisponibilidadeSabor()).equalsIgnoreCase("true")) {
            if (!cliente.isSubscribed(pizza)) {
                cliente.subscribeTo(pizza);
                clienteRepository.save(cliente);
                saborService.salvarSaborPizzaCadastrado(pizza);

                // Adicionando notificação dos observadores quando a pizza está disponível
                if (pizza.getDisponibilidadeSabor()) {
                    pizza.notifyObservers();
                }
            } else {
                throw new SaborPizzaClienteCadastrado();
            }
        } else {
            throw new SaborPizzaEstaDisponivel();
        }
    }

    private Cliente getClienteById(Long id) throws ClienteNaoEncontradoException {
        return clienteRepository.findById(id).orElseThrow(ClienteNaoEncontradoException::new);
    }

    private boolean isValidCodigoAcesso(String codigoAcesso) {
        return codigoAcesso.matches("[0-9]+") && codigoAcesso.length() == 6;
    }


}
