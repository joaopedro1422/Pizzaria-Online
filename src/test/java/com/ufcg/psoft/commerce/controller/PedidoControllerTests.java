package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Pedido.Pedido;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Cliente.ClienteRepository;
import com.ufcg.psoft.commerce.repository.Pedido.PedidoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import com.ufcg.psoft.commerce.service.Pedido.PedidoV1Service;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes da entidade Pedido")
public class PedidoControllerTests {

    final String URL_TEMPLATE = "/v1/pedidos";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    SaborRepository saborRepository;

    PedidoDTO pedidoDTO;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        pedidoRepository.deleteAll();

        clienteRepository.deleteAll();

        saborRepository.deleteAll();

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nome("Cliente de Exemplo")
                .endereco("Rua Exemplo, 123")
                .codigoAcesso("123456")
                .build();

        clienteRepository.save(objectMapper.convertValue(clienteDTO, Cliente.class));

        SaborPostPutDTO sabor1 = SaborPostPutDTO.builder()
                .saborDaPizza("Mussarela")
                .tipoDeSabor("DOCE")
                .valorMedia(10.0)
                .valorGrande(15.0)
                .disponibilidadeSabor(true)
                .build();

        SaborPostPutDTO sabor2 = SaborPostPutDTO.builder()
                .saborDaPizza("Mussarela")
                .tipoDeSabor("DOCE")
                .valorMedia(10.0)
                .valorGrande(15.0)
                .disponibilidadeSabor(true)
                .build();

        saborRepository.save(objectMapper.convertValue(sabor1, SaborPizza.class));
        saborRepository.save(objectMapper.convertValue(sabor2, SaborPizza.class));

    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação Pedido")
    class PedidoVerificacao {

        @Test
        @DisplayName("quando Criar Pedido com Dados Válidos então o pedido é criado e retornado")
        void quandoCriarPedidoComDadosValidos() throws Exception {
            // Arrange
            pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .codigoAcessoCliente("123456")
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoResultado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertNotNull(pedidoResultado.getId());
            assertTrue(pedidoResultado.getId() > 0);
            assertNotNull(pedidoResultado.getCodigoAcessoCliente());
            assertEquals(pedidoDTO.getCodigoAcesso(), pedidoResultado.getCodigoAcessoCliente());
            assertNotNull(pedidoResultado.getMetodoPagamento());
            assertNotNull(pedidoResultado.getEnderecoEntrega());
            assertEquals(pedidoDTO.getEnderecoEntrega(), pedidoResultado.getEnderecoEntrega());
        }

        @Test
        @DisplayName("quando Criar Pedido Com Dados Inválidos retorna badRequest com detalhes corretos")
        void quandoCriarPedidoComDadosInvalidos() throws Exception {
            // Arrange
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("")
                    .metodoPagamento("INVALIDO")
                    .enderecoEntrega("")
                    .pizzaIds(List.of()) // Lista de pizzas vazia
                    .build();

            // Act
            String resultadoStr = mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest()) // 400
                    .andReturn().getResponse().getContentAsString();

            // CustomErrorType customErrorType = objectMapper.readValue(resultadoStr, CustomErrorType.class);
            // Implemente a verificação dos erros de validação aqui
        }

        @Test
        @DisplayName("quando Criar Pedido com Código de Acesso Inválido, então lança exceção ClienteCodigoAcessoInvalidoException")
        void quandoCriarPedidoComCodigoAcessoInvalido() throws Exception {
            // Arrange: Código de acesso com menos de 6 dígitos
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .codigoAcesso("12345") // Código de acesso inválido
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            // Act e Assert: Deve lançar uma exceção ClienteCodigoAcessoInvalidoException
            mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest()); // 400
        }
    }

    @Nested
    @DisplayName("Atualização de Pedidos")
    class AtualizacaoDePedidos {

        @Test
        @DisplayName("quando Atualizar Pedido Com Dados Válidos então o pedido é atualizado com sucesso")
        void quandoAtualizarPedidoComDadosValidos() throws Exception {
            // Arrange
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            Pedido pedidoBase = pedidoRepository.save(objectMapper.convertValue(pedidoDTO, Pedido.class));

            // Atualizar os detalhes do pedido (exceto o código de acesso)
            pedidoDTO.setMetodoPagamento("CARTAO_DEBITO");
            pedidoDTO.setEnderecoEntrega("Avenida XYZ");

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoAtualizado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoAtualizado.getId());
            assertEquals(pedidoBase.getCodigoAcessoCliente(), pedidoAtualizado.getCodigoAcessoCliente()); // Código de acesso não deve ser atualizado
            assertEquals(pedidoDTO.getMetodoPagamento(), pedidoAtualizado.getMetodoPagamento());
            assertEquals(pedidoDTO.getEnderecoEntrega(), pedidoAtualizado.getEnderecoEntrega());
        }

        @Test
        @DisplayName("quando Atualizar Pedido Com Dados Inválidos retorna badRequest com detalhes corretos")
        void quandoAtualizarPedidoComDadosInvalidos() throws Exception {
            // Arrange
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("12345")
                    .metodoPagamento("INVALIDO")
                    .enderecoEntrega("")
                    .pizzaIds(List.of()) // Lista de pizzas vazia
                    .build();

            Pedido pedidoBase = pedidoRepository.save(objectMapper.convertValue(pedidoDTO, Pedido.class));

            // Atualizar os detalhes do pedido com dados inválidos
            pedidoDTO.setCodigoAcesso("");
            pedidoDTO.setMetodoPagamento("INVALIDO");
            pedidoDTO.setEnderecoEntrega("");

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pedidoBase.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest()) // 400
                    .andReturn().getResponse().getContentAsString();

            // CustomErrorType customErrorType = objectMapper.readValue(resultadoStr, CustomErrorType.class);
            // Implemente a verificação dos erros de validação aqui
        }

        @Test
        @DisplayName("quando Atualizar Pedido com Código de Acesso Inválido, então lança exceção ClienteCodigoAcessoInvalidoException")
        void quandoAtualizarPedidoComCodigoAcessoInvalido() throws Exception {
            // Arrange: Criar um pedido com código de acesso válido
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .codigoAcesso("123456") // Código de acesso válido
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            String resultadoStr = mockMvc.perform(post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoCriado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Atualizar o pedido com código de acesso inválido
            pedidoDTO.setCodigoAcesso("12345"); // Código de acesso inválido

            // Act e Assert: Deve lançar uma exceção ClienteCodigoAcessoInvalidoException
            mockMvc.perform(put(URL_TEMPLATE + "/" + pedidoCriado.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoDTO)))
                    .andExpect(status().isBadRequest()); // 400
        }
    }

    @Nested
    @DisplayName("Exclusão de Pedidos")
    class ExclusaoDePedidos {

        @Test
        @DisplayName("quando Excluir Pedido Existente então o pedido é removido com sucesso")
        void quandoExcluirPedidoExistente() throws Exception {
            // Arrange
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            Pedido pedidoBase = pedidoRepository.save(objectMapper.convertValue(pedidoDTO, Pedido.class));

            // Act
            mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + pedidoBase.getId()))
                    .andExpect(status().isNoContent()); // 204

            // Assert
            assertFalse(pedidoRepository.existsById(pedidoBase.getId()));
        }

        @Test
        @DisplayName("quando Excluir Pedido Inexistente retorna notFound")
        void quandoExcluirPedidoInexistente() throws Exception {
            // Arrange: Pedido com ID inexistente

            // Act
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
            PedidoDTO pedidoDTO = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            Pedido pedidoBase = pedidoRepository.save(objectMapper.convertValue(pedidoDTO, Pedido.class));

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/" + pedidoBase.getId()))
                    .andExpect(status().isOk()) // 200
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoEncontrado = objectMapper.readValue(resultadoStr, Pedido.class);

            // Assert
            assertEquals(pedidoBase.getId(), pedidoEncontrado.getId());
            assertEquals(pedidoDTO.getCodigoAcesso(), pedidoEncontrado.getCodigoAcessoCliente());
            assertEquals(pedidoDTO.getMetodoPagamento(), pedidoEncontrado.getMetodoPagamento());
            assertEquals(pedidoDTO.getEnderecoEntrega(), pedidoEncontrado.getEnderecoEntrega());
        }

        @Test
        @DisplayName("quando Buscar Pedido Inexistente retorna notFound")
        void quandoBuscarPedidoInexistente() throws Exception {
            // Arrange: Pedido com ID inexistente

            // Act
            mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999"))
                    .andExpect(status().isNotFound()); // 404
        }

        @Test
        @DisplayName("quando buscar por vários Pedidos Com Id válido então todos os Pedidos são retornados")
        void quandoBuscarVariosPedidosComIdValido() throws Exception {
            // Arrange
            PedidoDTO pedidoDTO1 = PedidoDTO.builder()
                    .codigoAcesso("123456")
                    .metodoPagamento("CARTAO_CREDITO")
                    .enderecoEntrega("Rua Nova, 123")
                    .pizzaIds(List.of(1L, 2L)) // IDs de pizzas válidos
                    .build();

            PedidoDTO pedidoDTO2 = PedidoDTO.builder()
                    .codigoAcesso("789101")
                    .metodoPagamento("PIX")
                    .enderecoEntrega("Avenida ABC, 456")
                    .pizzaIds(List.of(3L, 4L)) // IDs de pizzas válidos
                    .build();

            pedidoRepository.saveAll(List.of(
                    objectMapper.convertValue(pedidoDTO1, Pedido.class),
                    objectMapper.convertValue(pedidoDTO2, Pedido.class)
            ));

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)) // Header
                    .andExpect(status().isOk()) // 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> pedidos = objectMapper.readValue(resultadoStr, new TypeReference<List<Pedido>>() {
            });

            // Assert
            assertEquals(2, pedidos.size());
        }

        @Test
        @DisplayName("quando buscar por vários Pedidos Com Id Inválidos então retorna lista vazia")
        void quandoBuscarVariosPedidosComIdInvalidos() throws Exception {
            // Act
            mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999"))
                    .andExpect(status().isOk()) // 200
                    .andDo(print())
                    .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty()));
        }
    }
}
