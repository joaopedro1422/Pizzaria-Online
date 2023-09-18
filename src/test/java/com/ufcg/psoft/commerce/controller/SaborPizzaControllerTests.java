package com.ufcg.psoft.commerce.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborPostPutDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.enums.DisponibilidadeSabor;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;

import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Cliente.Cliente;
import com.ufcg.psoft.commerce.model.Entregador.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Sabores")
public class SaborPizzaControllerTests {
    final String URI_SABORES = "/pizza/v1/";

    @Autowired
    MockMvc driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    SaborPizza sabor;
    Estabelecimento estabelecimento;
    SaborPostPutDTO saborPostPutDTO;


    @BeforeEach
    void setup() {
        Set<SaborPizza> cardapio = new HashSet<SaborPizza>();
        Set<Entregador> entregadores = new HashSet<Entregador>();

        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .nome("bia")
                .cardapio(cardapio)
                .entregadores(entregadores)
                .codigoAcesso("65431")
                .build());
        sabor = saborRepository.save(SaborPizza.builder()
                .saborDaPizza("Calabresa")
                .tipoDeSabor(TipoDeSabor.SALGADO)
                .valorMedia(10.0)
                .valorGrande(15.0)
                .disponibilidadeSabor(DisponibilidadeSabor.DISPONIVEL)
                .build());
        saborPostPutDTO = SaborPostPutDTO.builder()
                .saborDaPizza(sabor.getSaborDaPizza())
                .tipoDeSabor(sabor.getTipoDeSabor())
                .valorMedia(sabor.getValorMedia())
                .valorGrande(sabor.getValorGrande())
                .disponibilidadeSabor(sabor.getDisponibilidadeSabor())
                .build();


    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Nested
    @DisplayName("verificacao da criacao de sabores de pizza")
    class SaborVerificacaoCriacao {
        @Test
        @DisplayName("quando criar sabor com dados validos e retorna que foi criado")
        void quandoCriarSaborComDadosValidos() throws Exception {
            // arrange
            // act
            String responseJsonString = driver.perform(post(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            SaborResponseDTO resultado = objectMapper.readValue(responseJsonString, SaborResponseDTO.class);

            // assert
            assertAll(
                    () -> assertEquals(saborPostPutDTO.getSaborDaPizza(), resultado.getSaborDaPizza()),
                    () -> assertEquals(saborPostPutDTO.getTipoDeSabor(), resultado.getTipoDeSabor()),
                    () -> assertEquals(saborPostPutDTO.getValorMedia(), resultado.getValorMedia()),
                    () -> assertEquals(saborPostPutDTO.getValorGrande(), resultado.getValorGrande()),
                    () -> assertEquals(saborPostPutDTO.getDisponibilidadeSabor(), resultado.getDisponibilidadeSabor())
            );


        }

    }

    @Nested
    @DisplayName("verificacao de excluir de sabores ")
    class SaborVerificacaoExcluir {
        @Test
        @DisplayName("Quando excluímos um sabor salvo")
        void quandoExcluimosSaborValido() throws Exception {
            // arrange


            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES + "/" + sabor.getIdPizza())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um sabor inexistente")
        void quandoExcluimosSaborInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES+ "/" + "99999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", "999999")
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um sabor passando código de acesso inválido")
        void quandoExcluimosSaborCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES +  "/" + sabor.getIdPizza())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", "999999"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
    @Nested
    @DisplayName("verificacao de listar de sabores ")
    class SaborVerificacaoListar{
            @Test
        @DisplayName("Quando buscamos por todos sabores salvos")
        void quandoBuscamosPorTodosSaboresSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 sabores no banco
            SaborPizza sabor1 = SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .valorMedia(10.0)
                    .valorGrande(15.0)
                    .disponibilidadeSabor(DisponibilidadeSabor.DISPONIVEL)
                    .build();
            SaborPizza sabor2 = SaborPizza.builder()
                    .saborDaPizza("Frango")
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .valorMedia(10.0)
                    .valorGrande(15.0)
                    .disponibilidadeSabor(DisponibilidadeSabor.DISPONIVEL)
                    .build();
            saborRepository.saveAll(Arrays.asList(sabor1, sabor2));

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
            }
        }

        /*@Test
        @DisplayName("Quando buscamos um sabor salvo pelo id")
        void quandoBuscamosPorUmSaborSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES + "/"+ estabelecimento.getCodigoAcesso()+"/" + sabor.getIdPizza())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertAll(
                    () -> assertEquals(sabor.getIdPizza().longValue(), resultado.getIdPizza().longValue()),
                    () -> assertEquals(sabor.getSaborDaPizza(), resultado.getSaborDaPizza()),
                    () -> assertEquals(sabor.getTipoDeSabor(), resultado.getTipoDeSabor()),
                    () -> assertEquals(sabor.getValorMedia(), resultado.getValorMedia()),
                    () -> assertEquals(sabor.getValorGrande(), resultado.getValorGrande()),
                    () -> assertEquals(sabor.getDisponibilidadeSabor(), resultado.getDisponibilidadeSabor())
            );
        }

        @Test
        @DisplayName("Quando buscamos um sabor inexistente")
        void quandoBuscamosPorUmSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES + "/"+ estabelecimento.getCodigoAcesso() + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos um sabor com código de acesso inválido")
        void quandoBuscamosPorUmSaborComCodigoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES +estabelecimento.getCodigoAcesso()+ "/" + sabor.getIdPizza())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", "999999")
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

    }

    @Nested
    @DisplayName("verificacao de alterar de sabores ")
    class SaborVerificacaoAlterar{
        @Test
        @DisplayName("Quando alteramos o sabor com dados válidos")
        void quandoAlteramosSaborValido() throws Exception {
            // Arrange
            Long idPizza = sabor.getIdPizza();

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", idPizza.toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertAll(
                    () -> assertEquals(sabor.getIdPizza().longValue(), resultado.getIdPizza().longValue()),
                    () -> assertEquals(sabor.getSaborDaPizza(), resultado.getSaborDaPizza()),
                    () -> assertEquals(sabor.getTipoDeSabor(), resultado.getTipoDeSabor()),
                    () -> assertEquals(sabor.getValorMedia(), resultado.getValorMedia()),
                    () -> assertEquals(sabor.getValorGrande(), resultado.getValorGrande()),
                    () -> assertEquals(sabor.getDisponibilidadeSabor(), resultado.getDisponibilidadeSabor())
            );
        }
        @Test
        @DisplayName("Quando alteramos um sabor inexistente")
        void quandoAlteramosSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", "999999")
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest())  // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor passando código de acesso inválido")
        void quandoAlteramosSaborCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", "999999")
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com nome válido")
        void quandoAlteramosSaborNomeValido() throws Exception {
            // Arrange
            saborPostPutDTO.setSaborDaPizza("Portuguesa");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertEquals("Portuguesa", resultado.getSaborDaPizza());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com nome vazio")
        void quandoAlteramosSaborNomeVazio() throws Exception {
            // Arrange
            saborPostPutDTO.setSaborDaPizza("");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("O sabor da pizza não pode estar em branco", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com tipo válido")
        void quandoAlteramosSaborTipoValido() throws Exception {
            // Arrange
            saborPostPutDTO.setTipoDeSabor(TipoDeSabor.SALGADO);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertEquals(TipoDeSabor.SALGADO, resultado.getTipoDeSabor());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com tipo nulo")
        void quandoAlteramosSaborTipoNulo() throws Exception {
            // Arrange
            saborPostPutDTO.setTipoDeSabor(null);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("O tipo da pizza não pode estar em branco", resultado.getErrors().get(0))
            );
        }


        @Test
        @DisplayName("Quando alteramos um sabor com precos válidos")
        void quandoAlteramosSaborPrecosValidos() throws Exception {
            // Arrange
            saborPostPutDTO.setValorMedia(40.0);
            saborPostPutDTO.setValorGrande(60.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertAll(
                    () -> assertEquals(40.0, resultado.getValorMedia()),
                    () -> assertEquals(60.0, resultado.getValorGrande())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos nulos")
        void quandoAlteramosSaborPrecosVazios() throws Exception {
            // Arrange
            saborPostPutDTO.setValorMedia(0.0);
            saborPostPutDTO.setValorGrande(0.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertTrue(resultado.getErrors().contains("O valor não pode estar em branco")),
                    () -> assertTrue(resultado.getErrors().contains("O valor não pode estar em branco"))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos inválidos")
        void quandoAlteramosSaborPrecosInvalidos() throws Exception {
            // Arrange
            saborPostPutDTO.setValorMedia(-10.0);
            saborPostPutDTO.setValorGrande(-250.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertTrue(resultado.getErrors().contains("PrecoM deve ser maior que zero")),
                    () -> assertTrue(resultado.getErrors().contains("PrecoG deve ser maior que zero"))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos válidos e inválidos")
        void quandoAlteramosSaborPrecosValidosEInvalidos() throws Exception {
            // Arrange
            saborPostPutDTO.setValorMedia(40.0);
            saborPostPutDTO.setValorGrande(-250.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("PrecoG deve ser maior que zero", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com disponibilidade válida")
        void quandoAlteramosSaborDisponibilidadeValida() throws Exception {
            // Arrange
            saborPostPutDTO.setDisponibilidadeSabor(DisponibilidadeSabor.INDISPONIVEL);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertFalse(resultado.isDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com disponibilidade nula")
        void quandoAlteramosSaborDisponibilidadeNula() throws Exception {
            // Arrange
            saborPostPutDTO.setDisponibilidadeSabor(null);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(saborPostPutDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Disponibilidade obrigatoria", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para false")
        void quandoAlteramosDisponibilidadeSaborFalse() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdPizza() + "/" + false)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertFalse(resultado.isDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para true")
        void quandoAlteramosDisponibilidadeSaborTrue() throws Exception {
            // Arrange
            sabor.setDisponibilidade(DisponibilidadeSabor.INDISPONIVEL);
            saborRepository.save(sabor);
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdPizza() + "/" + true)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborPizza resultado = objectMapper.readValue(responseJsonString, SaborPizza.class);

            // Assert
            assertTrue(resultado.isDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para false quando já está false")
        void quandoAlteramosDisponibilidadeSaborFalseQuandoJaEstaFalse() throws Exception {
            // Arrange
            sabor.setDisponibilidadeSabor(DisponibilidadeSabor.INDISPONIVEL);
            saborRepository.save(sabor);
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdPizza() + "/" + false)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado ja esta indisponivel!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para true quando já está true")
        void quandoAlteramosDisponibilidadeSaborTrueQuandoJaEstaTrue() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdPizza() + "/" + true)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", estabelecimento.getCodigoAcesso())
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado ja esta disponivel!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor com o código de acesso errado")
        void quandoAlteramosDisponibilidadeSaborCodigoErrado() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdPizza() + "/" + false)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPizza", sabor.getIdPizza().toString())
                            .param("idEstabelecimento", estabelecimento.getId().toString())
                            .param("codigoAcessoEstabelecimento", "aaaaaa")
                            .content(objectMapper.writeValueAsString(sabor)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }*/
    }
    
}
