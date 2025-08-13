package com.microservico.customer.application.useCases;

import com.microservico.customer.dto.request.ClienteDtoRequest;
import com.microservico.customer.dto.response.ClienteDtoResponse;

import java.util.List;

public interface ClienteUseCases {
    ClienteDtoResponse cadastrar(ClienteDtoRequest data);

    List<ClienteDtoResponse> buscarClientes();

    ClienteDtoResponse buscarClientePorId(Long id);

    ClienteDtoResponse atualizar(ClienteDtoRequest data, Long id);

    void excluirCliente(Long id);

}
