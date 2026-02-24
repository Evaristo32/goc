package br.com.goc.service;

import br.com.goc.model.Cliente;
import br.com.goc.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Cliente")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", "joao@example.com", "Rua A, 123");
        cliente.setClienteId(1L);
    }

    @Test
    @DisplayName("Deve salvar um novo cliente com sucesso")
    void testSalvarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.salvar(cliente);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve obter cliente por ID com sucesso")
    void testObterClientePorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obterPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve obter cliente por email com sucesso")
    void testObterClientePorEmailComSucesso() {
        when(clienteRepository.findByEmail("joao@example.com")).thenReturn(cliente);

        Cliente resultado = clienteService.obterPorEmail("joao@example.com");

        assertNotNull(resultado);
        assertEquals("joao@example.com", resultado.getEmail());
        verify(clienteRepository, times(1)).findByEmail("joao@example.com");
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void testDeletarClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        boolean resultado = clienteService.deletar(1L);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}

