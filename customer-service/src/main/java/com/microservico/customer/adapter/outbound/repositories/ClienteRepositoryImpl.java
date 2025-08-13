package com.microservico.customer.adapter.outbound.repositories;

import com.microservico.customer.adapter.outbound.entities.JpaClienteEntity;
import com.microservico.customer.model.Cliente;
import com.microservico.customer.application.repository.ClienteRepository;
import com.microservico.customer.util.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final JpaClienteRepository jpaClienteRepository;

    @Override
    public Cliente save(Cliente cliente) {
        JpaClienteEntity clienteEntity = new JpaClienteEntity(cliente);
         this.jpaClienteRepository.save(clienteEntity);
         return ClienteMapper.toEntity(clienteEntity);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        Optional<JpaClienteEntity> cliente = this.jpaClienteRepository.findById(id);
        return Optional.of(cliente.map(ClienteMapper::toEntity)).orElseThrow();
    }

    @Override
    public List<Cliente> findAll() {
        return this.jpaClienteRepository.findAll().stream().map(ClienteMapper::toEntity).toList();
    }

    @Override
    public void deleteById(Long id) {
        this.jpaClienteRepository.deleteById(id);
    }
}
