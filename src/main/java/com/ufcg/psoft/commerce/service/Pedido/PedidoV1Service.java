package com.ufcg.psoft.commerce.service.Pedido;

import java.util.List;

import com.ufcg.psoft.commerce.exception.Pedido.*;
import com.ufcg.psoft.commerce.exception.Pizza.PizzaSemSaboresException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.exception.Associacao.EstabelecimentoIdNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.PizzaRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;

@Service
public class PedidoV1Service implements PedidoService{

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pedido criarPedido(String codigoAcessoCliente, PedidoDTO pedidoDTO) {

        String codigoAcesso = pedidoDTO.getCodigoAcesso();

        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId()).orElseThrow(() -> new ClienteNaoEncontradoException());
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedidoDTO.getEstabelecimentoId()).orElseThrow(() -> new EstabelecimentoIdNaoEncontradoException());

        if (codigoAcesso == null || codigoAcesso.isEmpty() || !isValidCodigoAcesso(codigoAcesso)) {
            throw new PedidoCodigoAcessoIncorretoException();
        }

        if (codigoAcessoCliente == null || codigoAcessoCliente.isEmpty() || !isValidCodigoAcesso(codigoAcessoCliente)) {
            throw new PedidoCodigoAcessoInvalidoException();
        }

        List<Pizza> pizzas = pedidoDTO.getPizzas();

        // Verificar se a lista de pizzas está vazia
        if (pizzas == null || pizzas.isEmpty()) {
            throw new PizzaSemSaboresException();
        }

        double valorTotal = calcularValorTotal(pizzas);

        String enderecoEntrega = pedidoDTO.getEnderecoEntrega();
        if (enderecoEntrega == null || enderecoEntrega.isEmpty()) {
            enderecoEntrega = cliente.getEndereco();
        }

        if (!isValidMetodoPagamento(pedidoDTO.getMetodoPagamento())) {
            throw new MetodoPagamentoInvalidoException();
        }

        Pedido pedido = Pedido.builder()
                .codigoAcesso(codigoAcesso)
                .cliente(cliente)
                .estabelecimento(estabelecimento)
                .pizzas(pizzas)
                .enderecoEntrega(enderecoEntrega)
                .metodoPagamento(MetodoPagamento.valueOf(pedidoDTO.getMetodoPagamento()))
                .valorTotal(valorTotal)
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
                .build();

        // Setando o atributo pedido da classe Pizza, pois a pizza precisa guardar a informação
        // de qual pedido ela faz parte
        for(Pizza p : pedido.getPizzas()) {
            p.setPedido(pedido);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido atualizarPedido(Long pedidoId, String clienteCodigoAcesso, PedidoDTO pedidoDTO) {
        // Verificar se o pedido existe
        Pedido pedidoExistente = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException());

        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId()).orElseThrow(() -> new ClienteNaoEncontradoException());
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedidoDTO.getEstabelecimentoId()).orElseThrow(() -> new EstabelecimentoIdNaoEncontradoException());


        // Atualizar as informações do pedido
        String enderecoEntrega = pedidoDTO.getEnderecoEntrega();
        if (enderecoEntrega != null && !enderecoEntrega.isEmpty()) {
            pedidoExistente.setEnderecoEntrega(enderecoEntrega);
        } else {
            pedidoExistente.setEnderecoEntrega(cliente.getEndereco());
        }


        // Atualizar pizzas, se necessário
        if (pedidoDTO.getPizzas() != null && !pedidoDTO.getPizzas().isEmpty()) {
            // Verificar se a lista de pizzas está vazia
            if (pedidoDTO.getPizzas().stream().allMatch(pizza -> pizza.getSabor1() == null)) {
                throw new PizzaSemSaboresException();
            }

            pedidoExistente.setPizzas(pedidoDTO.getPizzas());
            // Atualizar o atributo pedido da classe Pizza, se necessário
            for (Pizza p : pedidoExistente.getPizzas()) {
                p.setPedido(pedidoExistente);
            }
        }

        // Atualizar método de pagamento, se necessário
        if (pedidoDTO.getMetodoPagamento() != null && !pedidoDTO.getMetodoPagamento().isEmpty()) {
            // Verificar se o método de pagamento é válido
            if (!isValidMetodoPagamento(pedidoDTO.getMetodoPagamento())) {
                throw new MetodoPagamentoInvalidoException();
            }

            pedidoExistente.setMetodoPagamento(MetodoPagamento.valueOf(pedidoDTO.getMetodoPagamento()));
        }




        // Calcular e atualizar o valor total
        double valorTotal = calcularValorTotal(pedidoExistente.getPizzas());
        pedidoExistente.setValorTotal(valorTotal);

        return pedidoRepository.save(pedidoExistente);
    }

    @Override
    public void removerPedido(Long pedidoId) throws PedidoNaoEncontradoException {
        Pedido pedidoExistente = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException());

        pedidoRepository.delete(pedidoExistente);
    }

    @Override
    public Pedido getPedido(Long id) throws PedidoNaoEncontradoException {
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNaoEncontradoException());
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

        if (!codigoAcesso.equals(pedido.getCliente())) {
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


//    private Cliente getClienteByCodigoAcesso(String codigoAcesso) {
//        Optional<Cliente> clienteOptional = clienteRepository.findByCodigoAcesso(codigoAcesso);
//        if (clienteOptional.isPresent()) {
//            return clienteOptional.get();
//        } else {
//            throw new ClienteNaoEncontradoException();
//        }
//    }
//
//    private List<Pizza> getPizzasByIds(List<Long> pizzaIds) {
//        List<Pizza> pizzas = new ArrayList<>();
//
//        for (Long pizzaId : pizzaIds) {
//            Optional<Pizza> pizzaOptional = pizzaRepository.findById(pizzaId);
//            if (pizzaOptional.isPresent()) {
//                Pizza pizza = pizzaOptional.get();
//                pizzas.add(pizza);
//            } else {
//                throw new SaborPizzaNaoEncontradoException();
//            }
//        }
//
//        return pizzas;
//    }


    private double calcularValorTotal(List<Pizza> pizzas) {
        double valorTotal = 0.0;

        for (Pizza pizza : pizzas) {
            valorTotal += pizza.calcularValorPizza();
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