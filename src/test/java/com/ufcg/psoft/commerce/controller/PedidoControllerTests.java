package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoResponseDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.enums.TamanhoPizza;
import com.ufcg.psoft.commerce.exception.Associacao.EstabelecimentoIdNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.Pedido.MetodoPagamentoInvalidoException;
import com.ufcg.psoft.commerce.exception.Pizza.PizzaSemSaboresException;
import com.ufcg.psoft.commerce.model.Associacao.Associacao;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.model.SaborPizza.Pizza;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.repository.Entregador.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes da entidade Pedido")
public class PedidoControllerTests {

    final String URL_CLIENTES = "/v1/clientes";
    final String URL_TEMPLATE = "/v1/pedidos";
    final String URI_ASSOCIACAO = "/associacao";
    final String URI_SABORES = "/pizza/v1/";

    final String URI_ESTABELECIMENTOS=  "/estabelecimentos";

    Estabelecimento estabelecimento;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    Entregador entregador;
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ObjectMapper objectMapper;

    PedidoDTO pedidoDTO;

    Pedido pedido;

    com.ufcg.psoft.commerce.enums.StatusPedido statusPedido;

    SaborPostPutDTO saborPostPutDTO;

    SaborPizza sabor;

    Cliente cliente;

    Pizza pizzaM;

    @BeforeEach
    void setup() {

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nome("Cliente de Exemplo")
                .endereco("Rua Exemplo, 123")
                .codigoAcesso("123456")
                .build();

        cliente = clienteRepository.save(objectMapper.convertValue(clienteDTO, Cliente.class));

        Set<SaborPizza> cardapio = new HashSet<>();
        Set<Entregador> entregadores = new HashSet<>();

        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .nome("bia")
                .saboresPizza(cardapio)
                .entregadores(entregadores)
                .codigoAcesso("654311")
                .build());
        sabor = saborRepository.save(SaborPizza.builder()
                .saborDaPizza("Calabresa")
                .tipoDeSabor("salgado")
                .valorMedia(10.0)
                .valorGrande(15.0)
                .disponibilidadeSabor(true)
                .estabelecimento(estabelecimento)
                .build());
        saborPostPutDTO = SaborPostPutDTO.builder()
                .saborDaPizza(sabor.getSaborDaPizza())
                .tipoDeSabor(sabor.getTipoDeSabor())
                .valorMedia(sabor.getValorMedia())
                .valorGrande(sabor.getValorGrande())
                .disponibilidadeSabor(sabor.getDisponibilidadeSabor())
                .build();

        pizzaM = Pizza.builder()
                .sabor1(sabor)
                .tamanho(TamanhoPizza.MEDIA)
                .build();


        List<Pizza> pizzas = List.of(pizzaM);

        pedido = pedidoRepository.save(Pedido.builder()
                .codigoAcesso("123456")
                .cliente(cliente.getId())
                .estabelecimento(estabelecimento.getId())
                .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                .enderecoEntrega("Rua nova,123")
                .pizzas(pizzas)
                .status(com.ufcg.psoft.commerce.enums.StatusPedido.PEDIDO_RECEBIDO)
                .build());



        pedidoDTO = PedidoDTO.builder()
                .codigoAcesso(pedido.getCodigoAcesso())
                .clienteId(pedido.getCliente())
                .estabelecimentoId(pedido.getEstabelecimento())
                .metodoPagamento(pedido.getMetodoPagamento())
                .enderecoEntrega(pedido.getEnderecoEntrega())
                .pizzas(pedido.getPizzas())
                .build();

        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Entregador Um")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Branco")
                .tipoVeiculo("Carro")
                .codigoAcesso("123456")
                .isDisponibilidade(true)
                .build());


    }

    @AfterEach
    void tearDown() {

        pedidoRepository.deleteAll();
        clienteRepository.deleteAll();
        saborRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        entregadorRepository.deleteAll();

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação Pedido")
    class PedidoVerificacao {

        @Test
        @DisplayName("quando Criar Pedido com Dados Válidos então o pedido é criado e retornado")
        void quandoCriarPedidoComDadosValidos() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();
            ;

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoResultado = objectMapper.readValue(resultadoStr, Pedido.class);
            pedidoRepository.save(pedidoResultado);
            // Assert
            //assertNotNull(pedidoResultado.getId());
            assertTrue(pedidoResultado.getId() > 0);
            assertEquals(cliente.getId(), pedidoResultado.getCliente());
            assertEquals(estabelecimento.getId(), pedidoResultado.getEstabelecimento());
            assertNotNull(pedidoResultado.getMetodoPagamento());
            assertNotNull(pedidoResultado.getEnderecoEntrega());
            assertEquals(pedidoDTO.getEnderecoEntrega(), pedidoResultado.getEnderecoEntrega());
        }

        @Test
        @DisplayName("quando Criar Pedido Com Dados Inválidos retorna badRequest")
        void quandoCriarPedidoComVariosDadosInvalidos() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("")
                    .pizzas(pizzas)
                    .build();

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoResultado = objectMapper.readValue(resultadoStr, Pedido.class);

        }

        @Test
        @DisplayName("quando Criar Pedido com Código de Acesso em Branco então lança exceção correspondente")
        void quandoCriarPedidoComCodigoAcessoEmBranco() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            // Configurar PedidoDTO com código de acesso em branco
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("")  // Dados inválidos: código de acesso em branco
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            // Act and Assert
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Verificar a exceção lançada
            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);

            // Verificar a mensagem de erro na resposta
            String responseContent = result.getResponse().getContentAsString();
            assertTrue(responseContent.contains("O código de acesso não pode estar vazio"));
        }

        @Test
        @DisplayName("quando Criar Pedido com Código de Acesso Inválido então lança exceção correspondente")
        void quandoCriarPedidoComCodigoAcessoInvalido() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            // Configurar PedidoDTO com código de acesso inválido
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("código_inválido")  // Dados inválidos: código de acesso inválido
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            // Act and Assert
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Verificar a exceção lançada
            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);

            // Verificar a mensagem de erro na resposta
            String responseContent = result.getResponse().getContentAsString();
            assertTrue(responseContent.contains("Código de acesso inválido"));
        }

//        @Test
//        @DisplayName("quando Criar Pedido com Pagamento em Branco então lança exceções correspondentes")
//        void quandoCriarPedidoComPagamentoEmBranco() throws Exception {
//            // Arrange
//            List<Pizza> pizzas = List.of(pizzaM);
//
//            // Configurar PedidoDTO com dados inválidos (método de pagamento em branco)
//            PedidoDTO pedidoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento("")  // Dados inválidos: método de pagamento em branco
//                    .enderecoEntrega("Rua Nova, 123")
//                    .pizzas(pizzas)
//                    .build();
//
//            // Act and Assert
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//
//            // Verificar a exceção lançada
//            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
//
//            // Verificar a mensagem de erro na resposta
//            String responseContent = result.getResponse().getContentAsString();
//            assertTrue(responseContent.contains("O método de pagamento não pode estar em branco/Inválido"));
//        }

//        @Test
//        @DisplayName("quando Criar Pedido com Pagamento Invalido então lança exceções correspondentes")
//        void quandoCriarPedidoComPagamentoInvalido() throws Exception {
//            // Arrange
//            List<Pizza> pizzas = List.of(pizzaM);
//
//            // Configurar PedidoDTO com dados inválidos (método de pagamento em branco)
//            PedidoDTO pedidoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)  // Dados inválidos: método de pagamento em branco
//                    .enderecoEntrega("Rua Nova, 123")
//                    .pizzas(pizzas)
//                    .build();
//
//            // Act and Assert
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//
//            // Verificar a exceção lançada
//            assertTrue(result.getResolvedException() instanceof MetodoPagamentoInvalidoException);
//
//            // Verificar a mensagem de erro na resposta
//            String responseContent = result.getResponse().getContentAsString();
//            assertTrue(responseContent.contains("Metodo de pagamento incorreto"));
//        }


        @Test
        @DisplayName("quando Criar Pedido com Estabelecimento Inválido então lança exceção correspondente")
        void quandoCriarPedidoComEstabelecimentoInvalido() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            // Configurar PedidoDTO com estabelecimento inválido
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(999L)  // Estabelecimento inexistente
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            // Act and Assert
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Verificar a exceção lançada
            assertTrue(result.getResolvedException() instanceof EstabelecimentoIdNaoEncontradoException);

            // Verificar a mensagem de erro na resposta
            String responseContent = result.getResponse().getContentAsString();
            assertTrue(responseContent.contains("O estabelecimento consultado nao existe!"));
        }

        @Test
        @DisplayName("quando Criar Pedido com Cliente Inválido então lança exceção correspondente")
        void quandoCriarPedidoComClienteInvalido() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            // Configurar PedidoDTO com cliente inválido
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(999L)  // Cliente inexistente
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            // Act and Assert
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Verificar a exceção lançada
            assertTrue(result.getResolvedException() instanceof ClienteNaoEncontradoException);

            // Verificar a mensagem de erro na resposta
            String responseContent = result.getResponse().getContentAsString();
            assertTrue(responseContent.contains("Cliente não Encontrado"));
        }

        @Test
        @DisplayName("quando Criar Pedido com Pizza Inválida então lança exceção correspondente")
        void quandoCriarPedidoComPizzaInvalida() throws Exception {
            // Arrange
            // Configurar PedidoDTO com pizza inválida (sem sabor)
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(Collections.emptyList())  // Dados inválidos: lista de pizzas vazia
                    .build();

            // Act and Assert
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Verificar a exceção lançada
            assertTrue(result.getResolvedException() instanceof PizzaSemSaboresException);

            // Verificar a mensagem de erro na resposta
            String responseContent = result.getResponse().getContentAsString();
            assertTrue(responseContent.contains("Sabor Nao Existe"));
        }

        @Test
        @DisplayName("quando Criar Pedido com Endereço de Entrega Vazio então é entregue no endereço principal")
        void quandoCriarPedidoComEnderecoDeEntregaVazio() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("")  // Endereço de entrega vazio
                    .pizzas(pizzas)
                    .build();

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoResultado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertNotNull(pedidoResultado.getId());
            assertTrue(pedidoResultado.getId() > 0);
            assertEquals(cliente.getId(), pedidoResultado.getCliente());
            assertEquals(estabelecimento.getId(), pedidoResultado.getEstabelecimento());
            assertNotNull(pedidoResultado.getMetodoPagamento());
            assertNotNull(pedidoResultado.getEnderecoEntrega());
            assertEquals(cliente.getEndereco(), pedidoResultado.getEnderecoEntrega());
        }
    }

    @Nested
    @DisplayName("Atualização de Pedidos")
    class AtualizacaoDePedidos {

        @Test
        @DisplayName("quando Atualizar Pedido com Novo Endereço de Entrega então o pedido é atualizado corretamente")
        void quandoAtualizarPedidoComNovoEnderecoEntrega() throws Exception {
            // Arrange
            // Cria um pedido inicial
            List<Pizza> pizzas = List.of(pizzaM);

            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);

            // Novo endereço de entrega
            String novoEndereco = "Rua Nova, 456";
            pedidoDTO.setEnderecoEntrega(novoEndereco);

            // Act
            resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoAtualizado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoAtualizado.getId());
            assertEquals(pedidoBase.getCodigoAcesso(), pedidoAtualizado.getCodigoAcesso()); // Código de acesso não deve ser atualizado
            assertEquals(novoEndereco, pedidoAtualizado.getEnderecoEntrega());
        }

        @Test
        @DisplayName("quando Atualizar Pedido com Método de Pagamento PIX então o pedido é atualizado corretamente")
        void quandoAtualizarPedidoComMetodoPagamentoPIX() throws Exception {
            // Arrange
            // Cria um pedido inicial
            List<Pizza> pizzas = List.of(pizzaM);

            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);

            // Novo método de pagamento PIX


            pedidoDTO.setMetodoPagamento(MetodoPagamento.PIX);

            // Act
            resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoAtualizado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoAtualizado.getId());
            assertEquals(pedidoBase.getCodigoAcesso(), pedidoAtualizado.getCodigoAcesso()); // Código de acesso não deve ser atualizado
            assertEquals(MetodoPagamento.PIX, pedidoAtualizado.getMetodoPagamento());
        }

        @Test
        @DisplayName("quando Atualizar Pedido com Método de Pagamento Débito então o pedido é atualizado corretamente")
        void quandoAtualizarPedidoComMetodoPagamentoDebito() throws Exception {
            // Arrange
            // Cria um pedido inicial
            List<Pizza> pizzas = List.of(pizzaM);

            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);



            pedidoDTO.setMetodoPagamento(MetodoPagamento.CARTAO_DEBITO);

            // Act
            resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoAtualizado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoAtualizado.getId());
            assertEquals(pedidoBase.getCodigoAcesso(), pedidoAtualizado.getCodigoAcesso()); // Código de acesso não deve ser atualizado
            assertEquals(MetodoPagamento.CARTAO_DEBITO, pedidoAtualizado.getMetodoPagamento());
        }

        @Test
        @DisplayName("quando Atualizar Pedido com Endereço de Entrega em Branco então utiliza o endereço principal do cliente")
        void quandoAtualizarPedidoComEnderecoEntregaEmBrancoUsaEnderecoPrincipalDoCliente() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            // Cria um pedido inicial
            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);

            // Novo endereço de entrega em branco
            pedidoDTO.setEnderecoEntrega("");

            // Act
            String resultadoAtualizadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoAtualizado = objectMapper.readValue(resultadoAtualizadoStr, Pedido.class);

            // Assert
            assertNotNull(pedidoAtualizado.getId());
            assertTrue(pedidoAtualizado.getId() > 0);
            assertEquals(cliente.getId(), pedidoAtualizado.getCliente());
            assertEquals(estabelecimento.getId(), pedidoAtualizado.getEstabelecimento());
            assertNotNull(pedidoAtualizado.getMetodoPagamento());
            assertNotNull(pedidoAtualizado.getEnderecoEntrega());
            assertEquals(cliente.getEndereco(), pedidoAtualizado.getEnderecoEntrega());
        }

//        @Test
//        @DisplayName("quando Atualizar Pedido com Pagamento Em Branco então lança exceções correspondentes")
//        void quandoAtualizarPedidoComPagamentoEmBranco() throws Exception {
//            // Arrange
//            List<Pizza> pizzas = List.of(pizzaM);
//
//            // Criar um pedido inicial
//            pedidoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento(null)
//                    .enderecoEntrega("Rua Nova, 123")
//                    .pizzas(pizzas)
//                    .build();
//
//            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andReturn().getResponse().getContentAsString();
//
//            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);
//
//            // Configurar PedidoDTO com dados inválidos (método de pagamento em branco)
//            PedidoDTO pedidoAtualizadoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)  // Dados inválidos: método de pagamento em branco
//                    .enderecoEntrega("Rua Nova, 456")
//                    .pizzas(pizzas)
//                    .build();
//
//            // Atualizar o pedido com dados inválidos
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
//                            .contentType(MediaType.APPLICATION_JSON_VALUE)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoAtualizadoDTO)))
//                    .andExpect(status().isNotFound())
//                    .andReturn();
//
//
//
//            // Verificar a mensagem de erro na resposta
//            String responseContent = result.getResponse().getContentAsString();
//            assertEquals(responseContent,"oi");
//            assertTrue(responseContent.contains("O método de pagamento não pode estar em branco/Inválido"));
//        }

//        @Test
//        @DisplayName("quando Atualizar Pedido com Pagamento Invalido então lança exceções correspondentes")
//        void quandoAtualizarPedidoComPagamentoInvalido() throws Exception {
//            // Arrange
//            List<Pizza> pizzas = List.of(pizzaM);
//
//            // Criar um pedido inicial
//            pedidoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento("invalido")
//                    .enderecoEntrega("Rua Nova, 123")
//                    .pizzas(pizzas)
//                    .build();
//
//            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoDTO)))
//                    .andExpect(status().isCreated())
//                    .andReturn().getResponse().getContentAsString();
//
//            Pedido pedidoBase = objectMapper.readValue(resultadoStr, Pedido.class);
//
//            // Configurar PedidoDTO com dados inválidos (método de pagamento em branco)
//            PedidoDTO pedidoAtualizadoDTO = PedidoDTO.builder()
//                    .codigoAcesso("123456")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)  // Dados inválidos: método de pagamento em branco
//                    .enderecoEntrega("Rua Nova, 456")
//                    .pizzas(pizzas)
//                    .build();
//
//            // Atualizar o pedido com dados inválidos
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
//                            .contentType(MediaType.APPLICATION_JSON_VALUE)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoAtualizadoDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//
//            assertTrue(result.getResolvedException() instanceof MetodoPagamentoInvalidoException);
//
//            // Verificar a mensagem de erro na resposta
//            String responseContent = result.getResponse().getContentAsString();
//            assertTrue(responseContent.contains("Metodo de pagamento incorreto"));
//        }
//

    }

    @Nested
    @DisplayName("Exclusão de Pedidos")
    class ExclusaoDePedidos {

        @Test
        @DisplayName("quando Excluir Pedido Existente então o pedido é removido com sucesso")
        void quandoExcluirPedidoExistente() throws Exception {
            // Criar o pedido
            List<Pizza> pizzas = List.of(pizzaM);

            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn();

            Pedido pedidoBase = objectMapper.readValue(result.getResponse().getContentAsString(), Pedido.class);

            // Excluir o pedido
            mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + pedidoBase.getId()))
                    .andExpect(status().isNoContent());


            // Verificar se o pedido foi removido
            assertFalse(pedidoRepository.existsById(pedidoBase.getId()));
        }

        @Test
        @DisplayName("quando Excluir Pedido Inexistente retorna notFound")
        void quandoExcluirPedidoInexistente() throws Exception {
            // Tentar excluir um pedido inexistente
            mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/999"))
                    .andExpect(status().isNotFound()); // 404
        }

    }

    @Nested
    @DisplayName("Busca de Pedidos")
    class BuscaDePedidos {

        @Test
        @DisplayName("quando Buscar Pedido Existente então o pedido é retornado")
        void quandoBuscarPedidoExistente() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            Pedido pedidoBase = objectMapper.readValue(result.getResponse().getContentAsString(), Pedido.class);

            // Act
            result = mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/" + pedidoBase.getId()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Pedido pedidoEncontrado = objectMapper.readValue(result.getResponse().getContentAsString(), Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoEncontrado.getId());
            assertEquals(pedidoDTO.getCodigoAcesso(), pedidoEncontrado.getCodigoAcesso());
            // ... outras verificações conforme necessário
        }

        @Test
        @DisplayName("quando Buscar Pedido por ID Inexistente então Retorna Not Found")
        void quandoBuscarPedidoPorIdInexistenteEntaoRetornaNotFound() throws Exception {
            // Act and Assert
            mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("quando Listar Pedidos então os pedidos são retornados")
        void quandoListarPedidos() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            PedidoDTO pedidoDTO1 = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            PedidoDTO pedidoDTO2 = PedidoDTO.builder()
                    .codigoAcesso("789012")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.PIX)
                    .enderecoEntrega("Rua Velha, 456")
                    .pizzas(pizzas)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO1)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO2)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // Act
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            // Assert
            List<Pedido> pedidosEncontrados = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Pedido>>() {
            });
            assertEquals(3, pedidosEncontrados.size());

            // Você pode adicionar mais verificações conforme necessário
            assertTrue(pedidosEncontrados.stream().anyMatch(pedido -> pedido.getCodigoAcesso().equals(pedidoDTO1.getCodigoAcesso())));
            //assertTrue(pedidosEncontrados.stream().anyMatch(pedido -> pedido.getCodigoAcesso().equals(pedidoDTO2.getCodigoAcesso())));
        }

        @Test
        @DisplayName("quando Listar Pedidos com ID Inválido então retorna Lista Vazia")
        void quandoListarPedidosComIdInvalido() throws Exception {
            // Arrange
            List<Pizza> pizzas = List.of(pizzaM);

            PedidoDTO pedidoDTO1 = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzas(pizzas)
                    .build();

            PedidoDTO pedidoDTO2 = PedidoDTO.builder()
                    .codigoAcesso("789012")
                    .clienteId(cliente.getId())
                    .estabelecimentoId(estabelecimento.getId())
                    .metodoPagamento(MetodoPagamento.CARTAO_DEBITO)
                    .enderecoEntrega("Rua Velha, 456")
                    .pizzas(pizzas)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO1)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO2)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // Act //Assert
            mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("conjunto de testes para verificaçao do status dos pedidos")
    class StatusPedido {
        @Test
        @DisplayName("Quando um pedido for criado, seu status deverá ser 'PEDIDO_RECEBIDO'")
        public void criaStatus() throws Exception {
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO pedidoResultado = objectMapper.readValue(resultadoStr, PedidoResponseDTO.PedidoResponseDTOBuilder.class).build();

            assertEquals(statusPedido.PEDIDO_RECEBIDO, pedidoResultado.getStatusPedido());
        }

        @Test
        @DisplayName("Quando o cliente confirmar o pagamento, o status do pedido devera ser alterado para PEDIDO_EM_PREPARO")
        public void preparaPedido() throws Exception {

            String metodo = String.valueOf(pedidoDTO.getMetodoPagamento());
            // confirma o pagamento
            String ResultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedido.getId() + "/confirmar-pagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("metodoPagamento", metodo)
                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO pedidoResultado = objectMapper.readValue(ResultadoStr, PedidoResponseDTO.PedidoResponseDTOBuilder.class).build();
            assertEquals(pedidoResultado.getStatusPedido(), statusPedido.PEDIDO_EM_PREPARO);
        }

        @Test
        @DisplayName("Quando o funcionario do estabelecimento confirmar que o pedido está pronto, o status devera ser PEDIDO_PRONTO")
        public void confirmaPedidoPronto() throws Exception{
            String ResultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPedido", String.valueOf(pedido.getId()))
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO pedidoResultado = objectMapper.readValue(ResultadoStr, PedidoResponseDTO.PedidoResponseDTOBuilder.class).build();
            assertEquals(pedidoResultado.getStatusPedido(), statusPedido.PEDIDO_PRONTO);
        }

        @Test
        @DisplayName("Quando o funcionario do estabelecimento atribuir o pedido a um entregador, o status deverá ser PEDIDO_EM_ROTA")
        public void enviaPedido() throws Exception{

            String responseJsonString = mockMvc.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            String responseJsonString2 = mockMvc.perform(put(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("idAssociacao", String.valueOf(resultado.getId())))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String ResultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/envia-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("idPedido", String.valueOf(pedido.getId()))
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO pedidoResultado = objectMapper.readValue(ResultadoStr, PedidoResponseDTO.PedidoResponseDTOBuilder.class).build();
            assertEquals(pedidoResultado.getStatusPedido(), statusPedido.PEDIDO_EM_ROTA);
        }

        @Test
        @DisplayName("Quando o cliente confirmar a entrega, o status do pedido devera ser PEDIDO_ENTREGUE")
        public void confirmaEntregaPedido()throws Exception{

            //cria uma associaçao para um entregador
            String responseJsonString = mockMvc.perform(post(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("entregadorId", entregador.getId().toString())
                            .param("codigoAcesso", entregador.getCodigoAcesso())
                            .param("estabelecimentoId", estabelecimento.getId().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            //aprova o entregador e o adiciona ao estabelecimento
            String responseJsonString2 = mockMvc.perform(put(URI_ASSOCIACAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("idAssociacao", String.valueOf(1)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // estabelecimento envia o pedido para o cliente atraves deste entregador
            String ResultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/envia-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("idPedido", String.valueOf(pedido.getId()))
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            //cliente confirma entrega
            String ResultadoStr3 = mockMvc.perform(MockMvcRequestBuilders.put(URL_CLIENTES + "/" + cliente.getId() + "/confirma-entrega")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", cliente.getCodigoAcesso())
                            .param("idPedido", String.valueOf(pedido.getId()))
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO pedidoResultado = objectMapper.readValue(ResultadoStr3, PedidoResponseDTO.PedidoResponseDTOBuilder.class).build();
            assertEquals(pedidoResultado.getStatusPedido(), statusPedido.PEDIDO_ENTREGUE);
        }

    }
}


