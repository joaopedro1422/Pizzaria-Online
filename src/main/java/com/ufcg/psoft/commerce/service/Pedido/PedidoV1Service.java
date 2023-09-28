package com.ufcg.psoft.commerce.service.Pedido;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;

import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.enums.TamanhoPizza;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoCodigoAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoMetodoPagamentoInvalidoException;
import com.ufcg.psoft.commerce.exception.Pedido.PedidoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pizza.*;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.PizzaRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoV1Service implements PedidoService{

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        String codigoAcesso = pedidoDTO.getCodigoAcesso();

        if (codigoAcesso == null || codigoAcesso.isEmpty() || !isValidCodigoAcesso(codigoAcesso)) {
            throw new PedidoCodigoAcessoInvalidoException();
        }

        // Verifique se o cliente com o código de acesso existe no banco de dados
        Cliente cliente = getClienteByCodigoAcesso(pedidoDTO.getCodigoAcessoCliente());

        if (cliente == null) {
            throw new ClienteNaoEncontradoException(); // Lançar exceção se o cliente não for encontrado
        }

        List<Pizza> pizzas = getPizzasByIds(pedidoDTO.getPizzaIds());
        double valorTotal = calcularValorTotal(pizzas);

        String enderecoEntrega = pedidoDTO.getEnderecoEntrega();
        if (enderecoEntrega == null || enderecoEntrega.isEmpty()) {
            enderecoEntrega = cliente.getEndereco();
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .pizzas(pizzas)
                .enderecoEntrega(enderecoEntrega)
                .codigoAcessoCliente(pedidoDTO.getCodigoAcessoCliente())
                .metodoPagamento(MetodoPagamento.valueOf(pedidoDTO.getMetodoPagamento()))
                .valorTotal(valorTotal)
                .build();

        pedido = pedidoRepository.save(pedido);

        return pedido;
    }



    @Override
    public Pedido atualizarPedido(Long id, PedidoDTO pedidoDTO) throws PedidoNaoEncontradoException {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();

            if (!pedido.getCliente().getCodigoAcesso().equals(pedidoDTO.getCodigoAcessoCliente())) {
                throw new PedidoCodigoAcessoIncorretoException();
            }

            List<Pizza> pizzas = getPizzasByIds(pedidoDTO.getPizzaIds());

            double novoValorTotal = calcularValorTotal(pizzas);

            pedido.setPizzas(pizzas);
            pedido.setEnderecoEntrega(pedidoDTO.getEnderecoEntrega());
            pedido.setMetodoPagamento(MetodoPagamento.valueOf(pedidoDTO.getMetodoPagamento()));
            pedido.setValorTotal(novoValorTotal);

            pedido = pedidoRepository.save(pedido);

            return pedido;
        } else {
            throw new PedidoNaoEncontradoException();
        }
    }


    @Override
    public void removerPedido(Long id) throws PedidoNaoEncontradoException {
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNaoEncontradoException();
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public Pedido getPedido(Long id) throws PedidoNaoEncontradoException {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        } else {
            throw new PedidoNaoEncontradoException();
        }
    }

    @Override
    public List<Pedido> getPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public void confirmarPagamento(Long id, String metodoPagamentoStr, String codigoAcesso)
            throws PedidoNaoEncontradoException, PedidoCodigoAcessoIncorretoException {
        Pedido pedido = getPedido(id);

        if (pedido == null) {
            throw new PedidoNaoEncontradoException();
        }

        if (!isValidMetodoPagamento(metodoPagamentoStr)) {
            throw new PedidoMetodoPagamentoInvalidoException();
        }

        if (!codigoAcesso.equals(pedido.getCodigoAcessoCliente())) {
            throw new PedidoCodigoAcessoIncorretoException();
        }

        StatusPedido statusPedido = StatusPedido.PAGAMENTO_CONFIRMADO;
        pedido.setStatus(statusPedido);

        MetodoPagamento metodoPagamento = MetodoPagamento.valueOf(metodoPagamentoStr);
        pedido.setMetodoPagamento(metodoPagamento);

        pedidoRepository.save(pedido);
    }



    @Override
    public boolean validarCodigoAcesso(Long id, String codigoAcesso) throws PedidoCodigoAcessoIncorretoException, PedidoNaoEncontradoException {
        Pedido pedido = getPedidoById(id);
        if (!pedido.getCodigoAcesso().equals(codigoAcesso)) {
            throw new PedidoCodigoAcessoIncorretoException();
        }
        return true;
    }


    private boolean isValidCodigoAcesso(String codigoAcesso) {
        return codigoAcesso.matches("[0-9]+") && codigoAcesso.length() == 6;
    }

    private Pedido getPedidoById(Long id) throws PedidoNaoEncontradoException {
        return pedidoRepository.findById(id).orElseThrow(PedidoNaoEncontradoException::new);
    }


    private Cliente getClienteByCodigoAcesso(String codigoAcesso) {
        Optional<Cliente> clienteOptional = clienteRepository.findByCodigoAcesso(codigoAcesso);
        if (clienteOptional.isPresent()) {
            return clienteOptional.get();
        } else {
            throw new ClienteNaoEncontradoException();
        }
    }

    private List<Pizza> getPizzasByIds(List<Long> pizzaIds) {
        List<Pizza> pizzas = new ArrayList<>();

        for (Long pizzaId : pizzaIds) {
            Optional<Pizza> pizzaOptional = pizzaRepository.findById(pizzaId);
            if (pizzaOptional.isPresent()) {
                Pizza pizza = pizzaOptional.get();
                pizzas.add(pizza);
            } else {
               throw new SaborPizzaNaoEncontradoException();
            }
        }

        return pizzas;
    }


    private double calcularValorTotal(List<Pizza> pizzas) {
        double valorTotal = 0.0;
        for (Pizza pizza : pizzas) {
            double valorPizza = calcularValorPizza(pizza);
            valorTotal += valorPizza;
        }
        return valorTotal;
    }


    private double calcularValorPizza(Pizza pizza) {
        List<SaborPizza> sabores = pizza.getSabores();

        if (sabores.isEmpty()) {
            throw new PizzaSemSaboresException();
        }

        double valorTotal = 0.0;

        if (pizza.getTamanho() == TamanhoPizza.GRANDE) {
            if (sabores.size() == 1) {
                valorTotal = sabores.get(0).getValorGrande();
            } else if (sabores.size() == 2) {
                double valor1 = sabores.get(0).getValorGrande();
                double valor2 = sabores.get(1).getValorGrande();
                valorTotal = (valor1 + valor2) / 2.0;
            } else {
                throw new PizzaGrandeComSaboresIncorretosException();
            }
        } else if (pizza.getTamanho() == TamanhoPizza.MEDIA) {
            if (sabores.size() != 1) {
                throw new PizzaMediaComSaboresIncorretosException();
            }
            valorTotal = sabores.get(0).getValorMedia();
        } else {
            throw new TamanhoPizzaInvalidoException();
        }

        return valorTotal;
    }

    private boolean isValidMetodoPagamento(String metodoPagamento) {
        MetodoPagamento[] metodosAceitaveis = MetodoPagamento.values();
        for (MetodoPagamento mp : metodosAceitaveis) {
            if (mp.name().equals(metodoPagamento)) {
                return true;
            }
        }
        return false;
    }


}