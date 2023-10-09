package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.dto.PizzaDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.enums.TipoDeSabor;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Estabelecimento.Estabelecimento;
import com.ufcg.psoft.commerce.model.SaborPizza.SaborPizza;
import com.ufcg.psoft.commerce.repository.Estabelecimento.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.Pizza.SaborRepository;
import jakarta.validation.constraints.AssertFalse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

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

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;

    EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO;

    ObjectMapper objectMapper = new ObjectMapper();

    Estabelecimento estabelecimento;

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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                    .build());

            estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
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
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por saborDaPizza (salgado)")
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Brigadeiro MM")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                    .build());
            

            estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", String.valueOf("salgado"))
                            .param("CodigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isOk()) // Codigo 150
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            List<SaborPizza> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(15.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Brigadeiro")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                    .build());

            estabelecimentoRepository.save(estabelecimento);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId() + "/sabores" + "/tipo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipo", String.valueOf("doce"))
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                    .andExpect(status().isOk()) // Codigo 150
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborPizza> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor02 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor03 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor04 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            estabelecimentoRepository.save(estabelecimento);


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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                    .build());

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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("doce")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());

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
                        .estabelecimento(estabelecimento)
                        .build());

                SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Mussarela")
                        .valorMedia(15.0)
                        .valorGrande(30.0)
                        .tipoDeSabor("salgado")
                        .disponibilidadeSabor(true)
                        .estabelecimento(estabelecimento)
                        .build());
                SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Chocolate")
                        .valorMedia(25.0)
                        .valorGrande(35.0)
                        .tipoDeSabor("doce")
                        .disponibilidadeSabor(true)
                        .estabelecimento(estabelecimento)
                        .build());

                SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Morango")
                        .valorMedia(15.0)
                        .valorGrande(30.0)
                        .tipoDeSabor("doce")
                        .disponibilidadeSabor(true)
                        .estabelecimento(estabelecimento)
                        .build());
                estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                        .codigoAcesso("123456")
                        .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                        .build());
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
                () -> assertEquals("Codigo de acesso invalido", resultado.getMessage())
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
                        .estabelecimento(estabelecimento)
                        .build());

                SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Mussarela")
                        .valorMedia(15.0)
                        .valorGrande(30.0)
                        .tipoDeSabor("salgado")
                        .disponibilidadeSabor(true)
                        .estabelecimento(estabelecimento)
                        .build());
                SaborPizza sabor3 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Chocolate")
                        .valorMedia(25.0)
                        .valorGrande(35.0)
                        .tipoDeSabor("doce")
                        .disponibilidadeSabor(false)
                        .estabelecimento(estabelecimento)
                        .build());

                SaborPizza sabor4 = saborRepository.save(SaborPizza.builder()
                        .saborDaPizza("Morango")
                        .valorMedia(15.0)
                        .valorGrande(30.0)
                        .tipoDeSabor("doce")
                        .disponibilidadeSabor(false)
                        .estabelecimento(estabelecimento)
                        .build());

                estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                        .codigoAcesso("123456")
                        .saboresPizza(Set.of(sabor1,sabor2,sabor3,sabor4))
                        .build());
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
                    .estabelecimento(estabelecimento)
                    .build());

            SaborPizza sabor2 = saborRepository.save(SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(15.0)
                    .valorGrande(30.0)
                    .tipoDeSabor("salgado")
                    .disponibilidadeSabor(true)
                    .estabelecimento(estabelecimento)
                    .build());
            estabelecimento.setSaboresPizza(Set.of(sabor1,sabor2));
            estabelecimentoRepository.save(estabelecimento);

            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getId()  + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSaborPizza", sabor1.getIdPizza().toString())
                            .param("codigoAcesso", estabelecimento.getCodigoAcesso())
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(estabelecimentoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();



            SaborResponseDTO resultado= objectMapper.readValue(responseJsonString, SaborResponseDTO.SaborResponseDTOBuilder.class).build();

            assertFalse(resultado.getDisponibilidadeSabor());

        }
    }

}