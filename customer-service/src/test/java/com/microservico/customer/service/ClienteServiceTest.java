package com.microservico.customer.service;

import com.microservico.customer.dto.request.ClienteDtoRequest;
import com.microservico.customer.dto.response.ClienteDtoResponse;
import com.microservico.customer.exceptions.RecursoNaoEncontradoException;
import com.microservico.customer.model.Cliente;
import com.microservico.customer.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    ClienteService clienteService;

    @Captor
    ArgumentCaptor<Cliente> clienteCaptor;

    @Test
    void deveCadastrarClienteERetornarDto() {
        // Given
        ClienteDtoRequest request = getClienteRequest();
        Cliente entidadeSalva = getCliente(1L);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(entidadeSalva);

        // When
        ClienteDtoResponse response = clienteService.cadastrar(request);

        // Then
        verify(clienteRepository).save(clienteCaptor.capture());
        Cliente clienteCaptor = this.clienteCaptor.getValue();
        assertThat(clienteCaptor.getNome()).isEqualTo(request.getNome());
        assertThat(clienteCaptor.getCpf()).isEqualTo(request.getCpf());
        assertThat(clienteCaptor.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(clienteCaptor.getEmail()).isEqualTo(request.getEmail());

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo(entidadeSalva.getNome());
    }

    @Test
    void deveBuscarTodosOsClientes() {
        // Given
        Cliente cliente = getCliente(1L);
        Cliente cliente1 = getCliente(2L);
        when(clienteRepository.findAll()).thenReturn(List.of(cliente, cliente1));

        // When
        List<ClienteDtoResponse> lista = clienteService.buscarClientes();

        // Then
        verify(clienteRepository).findAll();
        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).id()).isEqualTo(1L);
        assertThat(lista.get(1).id()).isEqualTo(2L);
    }

    @Test
    void deveBuscarClientePorId() {
        // Given
        Cliente c = getCliente(5L);
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(c));

        // When
        ClienteDtoResponse dto = clienteService.buscarClientePorId(5L);

        // Then
        verify(clienteRepository).findById(5L);
        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.nome()).isEqualTo(c.getNome());
    }

    @Test
    void deveLancarQuandoBuscarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> clienteService.buscarClientePorId(99L));
    }

    @Test
    void deveAtualizarTodosOsCamposDoCliente() {
        // Given
        ClienteDtoRequest dto = new ClienteDtoRequest(
                "Ana", "111.222.333-44", "1191234-5678", "ana@example.com");
        Cliente clienteExistente = getCliente(10L);
        when(clienteRepository.findById(10L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        // When
        ClienteDtoResponse response = clienteService.atualizar(dto, 10L);

        // Then
        verify(clienteRepository).save(clienteExistente);
        assertThat(response.nome()).isEqualTo("Ana");
        assertThat(response.cpf()).isEqualTo("111.222.333-44");
        assertThat(response.telefone()).isEqualTo("1191234-5678");
        assertThat(response.email()).isEqualTo("ana@example.com");
    }

    @Test
    void deveLancarQuandoAtualizarClienteInexistente() {
        when(clienteRepository.findById(50L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> clienteService.atualizar(getClienteRequest(), 50L));
    }

    @Test
    void deveExcluirClienteQuandoExistir() {
        // Given
        Cliente c = getCliente(20L);
        when(clienteRepository.findById(20L)).thenReturn(Optional.of(c));

        // When
        clienteService.excluirCliente(20L);

        // Then
        verify(clienteRepository).delete(c);
    }

    @Test
    void deveLancarQuandoExcluirClienteInexistente() {
        when(clienteRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> clienteService.excluirCliente(123L));
    }

    private Cliente getCliente(Long id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("1199999-0000");
        cliente.setEmail("joao@example.com");
        return cliente;
    }

    private ClienteDtoRequest getClienteRequest() {
        return new ClienteDtoRequest("Maria Santos", "987.654.321-00", "1198888-1111", "maria@example.com");
    }
}
