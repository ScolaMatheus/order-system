package com.microservico.customerservice.service;

import com.microservico.customerservice.dto.request.PedidoDtoRequest;
import com.microservico.customerservice.dto.response.PedidoDtoResponse;
import com.microservico.customerservice.event.PedidoCanceladoEvent;
import com.microservico.customerservice.event.PedidoStatusEvent;
import com.microservico.customerservice.exceptions.RecursoNaoEncontradoException;
import com.microservico.customerservice.exceptions.StatusIncorretoException;
import com.microservico.customerservice.mapper.PedidoMapper;
import com.microservico.customerservice.model.ItemPedido;
import com.microservico.customerservice.model.Pedido;
import com.microservico.customerservice.publisher.PedidoEventPublisher;
import com.microservico.customerservice.repositories.PedidoRepository;
import com.microservico.customerservice.util.StatusPedido;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PedidoService {

    private final String orderManagementUrl;
    private final PedidoEventPublisher pedidoEventPublisher;
    private final PedidoRepository pedidoRepository;

    public PedidoService(@Value("${order.management.url}") String orderManagementUrl, PedidoEventPublisher pedidoEventPublisher, PedidoRepository pedidoRepository) {
        this.orderManagementUrl = orderManagementUrl;
        this.pedidoEventPublisher = pedidoEventPublisher;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public PedidoDtoResponse criarPedido(PedidoDtoRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            PedidoDtoResponse dtoResponse = restTemplate.postForObject(
                    orderManagementUrl + "/api/pedidos",
                    request,
                    PedidoDtoResponse.class
            );

            if (dtoResponse != null) {
                Pedido pedido = new Pedido();

                pedido.setId(dtoResponse.id());
                pedido.setClienteId(dtoResponse.clienteId());
                pedido.setRestauranteId(request.getRestauranteId());
                pedido.setStatusPedido(dtoResponse.status());
                pedido.setValorTotal(dtoResponse.valorTotal());
                pedido.setItens(dtoResponse.itens().stream().map(itemPedidoDtoResponse -> new ItemPedido(
                        itemPedidoDtoResponse.id(),
                        pedido,
                        itemPedidoDtoResponse.produtoId(),
                        itemPedidoDtoResponse.nomeProduto(),
                        itemPedidoDtoResponse.precoUnitario(),
                        itemPedidoDtoResponse.quantidade()
                )).toList());
                pedido.setDataCriacao(dtoResponse.dataCriacao());
                pedido.setDataAtualizacao(LocalDateTime.now());

                return PedidoMapper.toDto(pedidoRepository.save(pedido));
            } else {
                throw new RecursoNaoEncontradoException("Houve um erro ao criar o pedido no OrderManagement.");
            }
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

    @Transactional
    public PedidoStatusEvent informarPedidoEntregue(Long idPedido) {
        // Buscar pedido.
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Não foi encontrado nenhum pedido para esse id: " + idPedido)
        );

        // Validando se o pedido está com o status correto
        if (pedido.getStatusPedido() == StatusPedido.EM_ROTA) {
            // Atualizando status do pedido na base.
            pedido.setStatusPedido(StatusPedido.ENTREGUE);
            pedidoRepository.save(pedido);
        } else {
            throw new StatusIncorretoException("O pedido: " + idPedido + " não está com o status correto para colocar o pedido como entregue. Status atual: " + pedido.getStatusPedido().name());
        }

        PedidoStatusEvent pedidoEvent = PedidoMapper.entityToEvent(pedido);

        pedidoEventPublisher.publicarPedidoEntregue(pedidoEvent);

        return pedidoEvent;
    }

    @Transactional
    public void atualizarPedido(Long idPedido, StatusPedido statusPedido, LocalDateTime dataHoraAtualizacao) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() != StatusPedido.CANCELADO) {
            pedido.setStatusPedido(statusPedido);
            pedido.setDataAtualizacao(dataHoraAtualizacao);
        }

        pedidoRepository.save(pedido);

        log.info("Pedido {} alterado para status {} às {}",idPedido, statusPedido, dataHoraAtualizacao);
    }

    public PedidoCanceladoEvent processarCancelamentoDePedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pedido não encontrado com esse id: " + idPedido));

        if (pedido.getStatusPedido() != StatusPedido.CANCELADO && pedido.getStatusPedido() != StatusPedido.EM_ROTA) {
            pedido.setStatusPedido(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
        } else {
            throw new StatusIncorretoException(
                    "O pedido: " + idPedido + " não pode ser cancelado pois ele já foi cancelado ou está a caminho da entrega. " +
                            "Status atual: " + pedido.getStatusPedido().name()
            );
        }

        PedidoCanceladoEvent canceladoEvent = new PedidoCanceladoEvent(
                pedido.getId(),
                pedido.getRestauranteId(),
                pedido.getClienteId(),
                StatusPedido.CANCELADO,
                LocalDateTime.now(),
                "Pedido cancelado por decisão do cliente",
                "customer-service"
        );

        pedidoEventPublisher.publicarPedidoCancelado(canceladoEvent);

        return canceladoEvent;
    }
}
