package com.microservico.customer.application.services;

import com.microservico.customer.application.repository.ClienteRepository;
import com.microservico.customer.application.useCases.ClienteUseCases;
import com.microservico.customer.dto.request.ClienteDtoRequest;
import com.microservico.customer.dto.response.ClienteDtoResponse;
import com.microservico.customer.exceptions.RecursoNaoEncontradoException;
import com.microservico.customer.util.mapper.ClienteMapper;
import com.microservico.customer.model.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClienteService implements ClienteUseCases{

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteDtoResponse cadastrar(ClienteDtoRequest dto){
        Cliente cliente = ClienteMapper.toEntity(dto);

        return ClienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    public List<ClienteDtoResponse> buscarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDto)
                .toList();
    }

    @Override
    public ClienteDtoResponse buscarClientePorId(Long idCliente){
        return clienteRepository.findById(idCliente)
                .map(ClienteMapper::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + idCliente));
    }

    @Override
    public ClienteDtoResponse atualizar(ClienteDtoRequest dto, Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    if (Objects.nonNull(dto.getNome())) cliente.setNome(dto.getNome());
                    if (Objects.nonNull(dto.getCpf())) cliente.setCpf(dto.getCpf());
                    if (Objects.nonNull(dto.getTelefone())) cliente.setTelefone(dto.getTelefone());
                    if (Objects.nonNull(dto.getEmail())) cliente.setEmail(dto.getEmail());

                    clienteRepository.save(cliente);

                    return ClienteMapper.toDto(cliente);
                }).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + id));
    }

    @Override
    public void excluirCliente(Long idCliente){
        clienteRepository.findById(idCliente).orElseThrow(
                () -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + idCliente)
        );

        clienteRepository.deleteById(idCliente);
    }
}
