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

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

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
                    .andExpect(status().isCreated()) // Codigo 201
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
                    .andExpect(status().isNoContent()) // Codigo 204
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
                    .andExpect(status().isOk()) // Codigo 200
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
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
                    //() -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
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

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento")
        void quandoBuscarCardapioEstabelecimento() throws Exception {
            // Arrange
            SaborPizza SaborPizza1 = SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();

            SaborPizza SaborPizza2 = SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();
            SaborPizza SaborPizza3 = SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();

            SaborPizza SaborPizza4 = SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(SaborPizza1, SaborPizza2, SaborPizza3, SaborPizza4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/SaborPizzaes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
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
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/SaborPizzaes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
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
        void quandoBuscarCardapioEstabelecimentoPorsaborDaPizza() throws Exception {
            // Arrange
            SaborPizza SaborPizza1 = SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();

            SaborPizza SaborPizza2 = SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();
            SaborPizza SaborPizza3 = SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();

            SaborPizza SaborPizza4 = SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(SaborPizza1, SaborPizza2, SaborPizza3, SaborPizza4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/SaborPizzaes" + "/tipoDeSabor")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("SaborPizza",String.valueOf(TipoDeSabor.SALGADO))
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
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
            SaborPizza SaborPizza1 = SaborPizza.builder()
                    .saborDaPizza("Calabresa")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();

            SaborPizza SaborPizza2 = SaborPizza.builder()
                    .saborDaPizza("Mussarela")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.SALGADO)
                    .build();
            SaborPizza SaborPizza3 = SaborPizza.builder()
                    .saborDaPizza("Chocolate")
                    .valorMedia(25.0)
                    .valorGrande(35.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();

            SaborPizza SaborPizza4 = SaborPizza.builder()
                    .saborDaPizza("Morango")
                    .valorMedia(20.0)
                    .valorGrande(30.0)
                    .tipoDeSabor(TipoDeSabor.DOCE)
                    .build();
            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .codigoAcesso("123456")
                    .saboresPizza(Set.of(SaborPizza1, SaborPizza2, SaborPizza3, SaborPizza4))
                    .build();
            estabelecimentoRepository.save(estabelecimento1);
            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getId() + "/SaborPizzaes" + "/tipoDeSabor")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("SaborPizza", String.valueOf(TipoDeSabor.DOCE))
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<SaborResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }
    }
}