package br.com.goc.service;

import br.com.goc.model.Cliente;
import br.com.goc.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obterPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> obterTodos() {
        return clienteRepository.findAll();
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setEndereco(clienteAtualizado.getEndereco());
            return clienteRepository.save(cliente);
        }
        return null;
    }

    public boolean deletar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Cliente obterPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}

