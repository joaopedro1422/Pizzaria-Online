package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteDTO;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteCodigoAcessoIncorretoException;
import com.ufcg.psoft.commerce.exception.Cliente.ClienteNaoEncontradoException;
import com.ufcg.psoft.commerce.service.Cliente.ClienteV1Service;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Clientes")
public class ClienteControllerTests {

    final String URL_TEMPLATE = "/v1/clientes";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClienteV1Service clienteService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class ClienteVerificacaoNome {

        @Test
        @DisplayName("quando Criar Cliente com Dados Válidos então o cliente é criado e retornado")
        void quandoCriarClienteComDadosValidos() throws Exception {
            // Arrange
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome("Novo Cliente");
            clienteDTO.setEndereco("Rua Nova, 123");
            clienteDTO.setCodigoAcesso("123456"); // Fornecendo um código de acesso válido

            // Act
            String resultadoStr = mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();

            ClienteDTO createdCliente = objectMapper.readValue(resultadoStr, ClienteDTO.class);

            // Assert
            assertNotNull(createdCliente.getId());
            assertTrue(createdCliente.getId() > 0);
            assertEquals("Novo Cliente", createdCliente.getNome());
            assertEquals("Rua Nova, 123", createdCliente.getEndereco());
            assertEquals("123456", createdCliente.getCodigoAcesso());
        }

    }
}
