package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.enums.MetodoPagamento;
import com.ufcg.psoft.commerce.enums.StatusPedido;
import com.ufcg.psoft.commerce.enums.TamanhoPizza;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de estabelecimentos")
public class EstabelecimentoControllerTests {
    @Autowired
    MockMvc driver;
    final String URI_ESTABELECIMENTOS = "/estabelecimentos";

    final String URI_PEDIDOS= "/v1/pedidos";

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PedidoRepository pedidoRepository;


    EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO;

    ObjectMapper objectMapper = new ObjectMapper();

    Estabelecimento estabelecimento;

    PedidoDTO pedidoDTO;

    SaborPostPutDTO saborPostPutDTO;

    SaborPizza sabor;

    Pedido pedido;

    Cliente cliente;

    Pizza pizzaM;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("123456")
                .build());


    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class EstabelecimentoVerificacaoFluxosBasicosApiRest {
        final String URI_ESTABELECIMENTOS = "/estabelecimentos";
        EstabelecimentoPostPutRequestDTO estabelecimentoPutRequestDTO;
        EstabelecimentoPostPutRequestDTO estabelecimentoPostRequestDTO;

        @BeforeEach
        void setup() {
            estabelecimentoPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("123456")
                    .build();
            estabelecimentoPostRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("654321")
                    .build();
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados válidos")
        void quandoCriarEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 151
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoResponseDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoResponseDTO.EstabelecimentoResponseDTOBuilder.class).build();
            System.out.println(resultado.getCodigoAcesso());
            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(estabelecimentoPostRequestDTO.getCodigoAcesso(), resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando excluímos um estabelecimento salvo")
        void quandoExcluimosEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isNoContent()) // Codigo 154
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando atualizamos um estabelecimento salvo")
        void quandoAtualizamosEstabelecimentoValido() throws Exception {
            // Arrange
            estabelecimentoPutRequestDTO.setCodigoAcesso("131289");

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 150
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoResponseDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoResponseDTO.EstabelecimentoResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), estabelecimento.getId().longValue()),
                    () -> assertEquals("131289", resultado.getCodigoAcesso())
            );
        }

        @Test
        @DisplayName("Quando alteramos um estabelecimento com codigo de acesso inválido")
        void quandoAlterarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos")
        void quandoCriarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                    .codigoAcesso("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoAcesso", estabelecimentoPostPutRequestDTO.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);
            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
                    //() -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação sobre cardapio no estabelecimento")
    class EstabelecimentoCardapioVerificacoes {
        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento")
        void quandoBuscarCardapioEstabelecimento() throws Exception {
            // Arrange

            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 150
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            Estabelecimento resultado = objectMapper.readValue(responseJsonString, Estabelecimento.class);
            var quantidadeDeSabores = resultado.getSaboresPizza().size();
            // Assert
            assertAll(
                    () -> assertEquals(4, quantidadeDeSabores)
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)

                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardápio de um estabelecimento por saborDaPizza (salgado)")
        void quandoBuscarCardapioEstabelecimentoPorTipo() throws Exception {
            // Arrange
            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .build());
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Brigadeiro MM")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimento = estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            Estabelecimento resultado = objectMapper.readValue(responseJsonString, Estabelecimento.class);

            // Filtrar os sabores salgados
            List<SaborPizza> saboresSalgados = resultado.getSaboresPizza().stream()
                    .filter(sabor -> "salgado".equals(sabor.getTipoDeSabor()))
                    .collect(Collectors.toList());

            // Assert
            assertEquals(2, saboresSalgados.size());
        }


        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por saborDaPizza (doce)")
        void quandoBuscarCardapioEstabelecimentoPorsaborDaPizzaDoce() throws Exception {
            // Arrange

            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(15.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Brigadeiro")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4));
            estabelecimento = estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            Estabelecimento resultado = objectMapper.readValue(responseJsonString, Estabelecimento.class);

            // Filtrar os sabores salgados
            List<SaborPizza> saboresDoces = resultado.getSaboresPizza().stream()
                    .filter(sabor -> "doce".equals(sabor.getTipoDeSabor()))
                    .collect(Collectors.toList());

            // Assert
            assertEquals(2, saboresDoces.size());
        }

        @Test
        @DisplayName("Quando buscamos o cardapio salgadode um estabelecimento com o id estabelecimento invalido")
        void quandoBuscarCardapioSalgadoEstabelecimentoIdInvalido() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimentoRepository.save(estabelecimento);


            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 300 + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "salgado")
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio doce  de um estabelecimento com o id estabelecimento invalido")
        void quandoBuscarCardapioDoceEstabelecimentoIdInvalido() throws Exception {
            // Arrange
            SaborPizza sabor01 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor02 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor03 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor04 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());




            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 300 + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", "doce")
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", result.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio  de um estabelecimento com tipo de sabor invalido")
        void quandoBuscarCardapioEstabelecimentoTipoInvalido() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);


            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo","caramelizada")
                            .param("codigoAcesso",estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
            );

        }

        @Test
        @DisplayName("Quando buscamos o cardapio  de um estabelecimento por disponibilidade true")
        void quandoBuscarCardapioEstabelecimentoPorDisponibilidadeTrue() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());


            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);


            //act

            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade", "true")
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isOk()) // Codigo 150
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborPizza> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });


            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size())
            );
        }
        @Test
        @DisplayName("Quando buscamos o cardapio  de um estabelecimento por disponibilidade com id invalido")
        void quandoBuscarCardapioEstabelecimentoPorDisponibilidadeComIdInvalido() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);

            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 60 + "/sabores" + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade","true")
                            .param("codigoAcesso",estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }


        @Test
        @DisplayName("Quando buscamos o cardapio  de um estabelecimento por disponibilidade com codigo de acesso invalido")
        void quandoBuscarCardapioEstabelecimentoPorDisponibilidadeComCodigoAcessoInvalido() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);

            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade","true")
                            .param("codigoAcesso","2222"))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio  de um estabelecimento por disponibilidade false")
        void quandoBuscarCardapioEstabelecimentoPorDisponibilidadeFalse() throws Exception {
            // Arrange
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(false)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(false)
                    //.estabelecimento(estabelecimento)
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1, sabor2, sabor3, sabor4));
            estabelecimentoRepository.save(estabelecimento);


            //act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("disponibilidade","false")
                            .param("codigoAcesso",estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborPizza> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

    }
    @Nested
    @DisplayName("Métodos pagamento")
    class testesMetodoPagamento {

        @Test
        @DisplayName("Teste do pagamento por pix")
        public void porPixTest(){

            assertTrue(false);

        }


        @Test
        @DisplayName("Teste do pagamento por cartao de credito")
        public void porCreditoTest(){
            assertTrue(false);
        }

        @Test
        @DisplayName("Teste do pagamento por debito")
        public void porDebitoTest(){
            assertTrue(false);
        }

        @Test
        @DisplayName("Teste codigo de acesso passado indevidamente")
        public void codigoAcessoPedidoInvalidoTest(){
            assertTrue(false);
        }


        @Test
        @DisplayName("Teste codigo de acesso estabelecimento passado indevidamente")
        public void codigoAcessoEstabelecimentoInvalidoTest(){
            assertTrue(false);
        }

        @Test
        @DisplayName("Teste pedido não existe")
        public void pedidoInexistenteTest(){
            assertTrue(false);
        }

    }

    @Nested
    @DisplayName("Testes para alteraçoes de disponibilidade dos sabores do cardapio do estabelecimento")
    class disponibilidadeCardapio{

        @Test
        @DisplayName("Teste alterar disponibilidade do sabor no cardapio")
        public void alterarDisponibilidadeSaborCardapio() throws Exception{
            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    //.estabelecimento(estabelecimento)
                    .build());
            estabelecimento.setSaboresPizza(Set.of(sabor1,sabor2));
            estabelecimentoRepository.save(estabelecimento);

            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId()  + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSaborPizza", sabor1.getIdPizza().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(sabor2)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();



            SaborResponseDTO resultado= objectMapper.readValue(responseJsonString, SaborResponseDTO.class);
            //o sabor2 como indisponivel e o sabor1 como disponivel
            assertFalse(resultado.getDisponibilidadeSabor());
            assertTrue(sabor1.getDisponibilidadeSabor());
        }

        @Test
        @DisplayName("Teste para notificaçao dos clientes interessados nos sabores indisponiveis")
        public void notificarClientesInteressados() throws Exception{

            Cliente clienteUm = Cliente.builder()
                    .nome("Cliente1")
                    .endereco("Rua Nova, 123")
                    .codigoAcesso("123456")
                    .build();

            Cliente clienteDois = Cliente.builder()
                    .nome("Cliente2")
                    .endereco("Rua velha, 123")
                    .codigoAcesso("019999")
                    .build();

            SaborPizza sabor1 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(false)
                    //.estabelecimento(estabelecimento)
                    .observers(new ArrayList<>())
                    .build());

            estabelecimento.setSaboresPizza(Set.of(sabor1));
            estabelecimentoRepository.save(estabelecimento);

            clienteUm.subscribeTo(sabor1);
            clienteDois.subscribeTo(sabor1);


            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId()  + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSaborPizza", sabor1.getIdPizza().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("disponibilidade", objectMapper.writeValueAsString(true))
                            .content(objectMapper.writeValueAsString(sabor1)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborResponseDTO resultado= objectMapper.readValue(responseJsonString, SaborResponseDTO.class);

            sabor1.notifyObservers();
            assertEquals(2,sabor1.observersSize());


        }
    }

    @Nested
    @DisplayName("Conjunto de testes para atualizaçoes do status do pedido")
    class statusPedido {
        @Test
        @DisplayName("Quando um pedido for criado, seu status devera indicar que o pedido foi recebido pelo estabelecimento")
        public void pedidoRecebido() throws Exception {


        }
    }

//    @Nested
//    @DisplayName("Testes para notificar o cliente sobre o pedido em rota")
//    class notificarCliente {
//        @Test
//        @DisplayName("Quando atribuo um entregador existente a um pedido existente")
//        @Transactional
//        public void notificarClienteTudoValido() throws Exception {
//            //arrange
//            ClienteDTO clienteDTO = ClienteDTO.builder()
//                    .nome("Cliente de Exemplo")
//                    .endereco("Rua Exemplo, 123")
//                    .codigoAcesso("123456")
//                    .build();
//
//            cliente = clienteRepository.save(objectMapper.convertValue(clienteDTO, Cliente.class));
//
//            Set<SaborPizza> cardapio = new HashSet<>();
//            Set<Entregador> entregadores = new HashSet<>();
//
//            objectMapper.registerModule(new JavaTimeModule());
//            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
//                    .nome("bia")
//                    .saboresPizza(cardapio)
//                    .entregadores(entregadores)
//                    .codigoAcesso("654311")
//                    .build());
//            sabor = saborRepository.save(SaborPizza.builder()
//                    .saborDaPizza("Calabresa")
//                    .tipoDeSabor("salgado")
//                    .valorMedia(10.0)
//                    .valorGrande(15.0)
//                    .disponibilidadeSabor(true)
//                    .build());
//            saborPostPutDTO = SaborPostPutDTO.builder()
//                    .saborDaPizza(sabor.getSaborDaPizza())
//                    .tipoDeSabor(sabor.getTipoDeSabor())
//                    .valorMedia(sabor.getValorMedia())
//                    .valorGrande(sabor.getValorGrande())
//                    .disponibilidadeSabor(sabor.getDisponibilidadeSabor())
//                    .build();
//
//            pizzaM = Pizza.builder()
//                    .sabor1(sabor)
//                    .tamanho(TamanhoPizza.MEDIA)
//                    .build();
//
//
//            List<Pizza> pizzas = List.of(pizzaM);
//
//            pedido = pedidoRepository.save(Pedido.builder()
//                    .codigoAcesso("123456")
//                    .cliente(cliente.getId())
//                    .estabelecimento(estabelecimento.getId())
//                    .metodoPagamento(MetodoPagamento.CARTAO_CREDITO)
//                    .enderecoEntrega("Rua nova,123")
//                    .pizzas(pizzas)
//                    .status(com.ufcg.psoft.commerce.enums.StatusPedido.PEDIDO_RECEBIDO)
//                    .build());
//
//            pedidoDTO = PedidoDTO.builder()
//                    .id(pedido.getId())
//                    .codigoAcesso(pedido.getCodigoAcesso())
//                    .clienteId(pedido.getCliente())
//                    .estabelecimentoId(pedido.getEstabelecimento())
//                    .metodoPagamento(pedido.getMetodoPagamento())
//                    .enderecoEntrega(pedido.getEnderecoEntrega())
//                    .pizzas(pedido.getPizzas())
//                    .build();
//
//            Entregador entregador = entregadorRepository.save(Entregador.builder()
//                    .nome("Entregador Um")
//                    .placaVeiculo("ABC-1234")
//                    .corVeiculo("Branco")
//                    .tipoVeiculo("Carro")
//                    .codigoAcesso("123456")
//                    .isDisponibilidade(true)
//                    .build());
//
//            // act
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PrintStream printStream = new PrintStream(outputStream);
//            PrintStream originalSystemOut = System.out;
//            System.setOut(printStream);
//
//
//            String resultadoStr = driver.perform(MockMvcRequestBuilders.get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/notificarCliente")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("idPedido", String.valueOf(pedido.getId()))
//                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
//                            .param("id", estabelecimento.getId().toString()))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andReturn().getResponse().getContentAsString();
//
//            Pedido pedidoAtualizado = objectMapper.readValue(resultadoStr, Pedido.class);
//
//            assertEquals(pedidoAtualizado.getStatus(), com.ufcg.psoft.commerce.enums.StatusPedido.PEDIDO_EM_ROTA);
//            assertNotNull(pedidoAtualizado.getEntregador());
//
//            System.setOut(originalSystemOut); // Restaurar a saída do System.out
//
//            String resultadoPrint = outputStream.toString().trim();
//
//            String notificacaoEsperada = " - PEDIDO EM ROTA - \n" +
//                    "Cliente: " + cliente.getNome() + "\n" +
//                    "Entregador: " + entregador.getNome() + "\n" +
//                    "Tipo do Veiculo: " + entregador.getTipoVeiculo() + "\n" +
//                    "Cor do Veiculo: " + entregador.getCorVeiculo() + "\n" +
//                    "Placa do Veiculo: " + entregador.getPlacaVeiculo();
//
//            assertTrue(resultadoPrint.contains(notificacaoEsperada));
//        }
//
//        @Test
//        @DisplayName("Quando atribuo um entregador existente a um pedido existente")
//        @Transactional
//        public void test01() throws Exception {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PrintStream printStream = new PrintStream(outputStream);
//            PrintStream originalSystemOut = System.out;
//            System.setOut(printStream);
//
//            String respostaJson2 = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/notificarCliente")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
//                            .param("id", estabelecimento.getId().toString()))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            Pedido pedidoAtualizado = objectMapper.readValue(respostaJson2, Pedido.class);
//
//            assertEquals(pedidoAtualizado.getStatus(), com.ufcg.psoft.commerce.enums.StatusPedido.PEDIDO_EM_ROTA);
//            assertNotNull(pedidoAtualizado.getEntregador());
//
//            try {
//                String resultadoPrint = outputStream.toString();
//
//                String regex = "Hibernate: .*";
//
//                String resultadoFiltrado = resultadoPrint.replaceAll(regex, "").trim();
//
//                String notificacaoEsperada = """
//                        Joao, seu pedido está em rota de entrega
//                        --Informações do entregador--:
//                        Nome: Jose da Silva
//                        Tipo de Veiculo: MOTO
//                        Cor do Veiculo: Branco
//                        Placa do Veiculo: 123456""";
//                assertTrue(resultadoFiltrado.contains(notificacaoEsperada));
//            } finally {
//                System.setOut(originalSystemOut);
//            }
//
//        }
//    }
}