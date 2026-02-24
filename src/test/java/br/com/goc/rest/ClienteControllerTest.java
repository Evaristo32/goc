package br.com.goc.rest;

import br.com.goc.model.Cliente;
import br.com.goc.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@DisplayName("Testes do Controller de Cliente")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", "joao@example.com", "Rua A, 123");
        cliente.setClienteId(1L);
    }

    @Test
    @DisplayName("Deve criar novo cliente com sucesso")
    void testCriarClienteComSucesso() throws Exception {
        when(clienteService.salvar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated());

        verify(clienteService, times(1)).salvar(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar cliente inválido")
    void testCriarClienteComErro() throws Exception {
        when(clienteService.salvar(any(Cliente.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest());
    }
}

