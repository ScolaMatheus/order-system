package com.microservico.customerservice.service;


import com.microservico.customerservice.dto.request.ClienteDtoRequest;
import com.microservico.customerservice.dto.response.ClienteDtoResponse;
import com.microservico.customerservice.exceptions.RecursoNaoEncontradoException;
import com.microservico.customerservice.mapper.ClienteMapper;
import com.microservico.customerservice.model.Cliente;
import com.microservico.customerservice.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDtoResponse cadastrar(ClienteDtoRequest dto){
        Cliente cliente = ClienteMapper.toEntity(dto);

        Cliente clienteCadastrado = clienteRepository.save(cliente);
        return ClienteMapper.toDto(clienteCadastrado);
    }

    public List<ClienteDtoResponse> buscarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDto)
                .toList();
    }

    public ClienteDtoResponse buscarClientePorId(Long idCliente){
        return clienteRepository.findById(idCliente)
                .map(ClienteMapper::toDto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + idCliente));
    }

    public ClienteDtoResponse atualizar(ClienteDtoRequest dto, Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    if (Objects.nonNull(dto.getNome())) cliente.setNome(dto.getNome());
                    if (Objects.nonNull(dto.getCpf())) cliente.setCpf(dto.getCpf());
                    if (Objects.nonNull(dto.getTelefone())) cliente.setTelefone(dto.getTelefone());
                    if (Objects.nonNull(dto.getEmail())) cliente.setEmail(dto.getEmail());
                    return ClienteMapper.toDto(clienteRepository.save(cliente));
                }).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + id));
    }

    public void excluirCliente(Long idCliente){
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(
                () -> new RecursoNaoEncontradoException("Cliente não encontrado com id: " + idCliente)
        );

        clienteRepository.delete(cliente);
    }
}
