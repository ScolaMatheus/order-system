package com.microservico.customerservice.service;

import com.microservico.customerservice.dto.request.PedidoDtoRequest;
import com.microservico.customerservice.dto.response.PedidoDtoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoClient {

    @Value("${order.management.url}")
    private String orderManagementUrl;

    public PedidoDtoResponse criarPedido(PedidoDtoRequest request) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.postForObject(orderManagementUrl + "/api/pedidos", request, PedidoDtoResponse.class);

        } catch (HttpClientErrorException e) {
            log.warn("Erro 4xx ao criar pedido: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Pedido inválido");
        } catch (HttpServerErrorException e) {
            log.error("Erro 5xx no OrderManagement: {}", e.getStatusCode());
            throw new RuntimeException("Erro no sistema de pedidos");
        } catch (ResourceAccessException e) {
            log.error("Timeout ou falha de rede ao acessar OrderManagement: {}", e.getMessage());
            throw new RuntimeException("Serviço de pedidos indisponível");
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            log.error("Erro inesperado ao criar pedido", e);
            throw new RuntimeException("Erro ao criar pedido");
        }

    }
}
