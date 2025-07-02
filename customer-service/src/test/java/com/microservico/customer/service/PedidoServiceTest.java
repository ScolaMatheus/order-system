package com.microservico.customer.service;

import com.microservico.customer.dto.request.ItemPedidoDtoRequest;
import com.microservico.customer.dto.request.PedidoDtoRequest;
import com.microservico.customer.dto.response.PedidoDtoResponse;
import com.microservico.customer.event.PedidoCanceladoEvent;
import com.microservico.customer.event.PedidoStatusEvent;
import com.microservico.customer.exceptions.RecursoNaoEncontradoException;
import com.microservico.customer.exceptions.StatusIncorretoException;
import com.microservico.customer.model.ItemPedido;
import com.microservico.customer.model.Pedido;
import com.microservico.customer.publisher.PedidoEventPublisher;
import com.microservico.customer.repositories.PedidoRepository;
import com.microservico.customer.util.StatusPedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.microservico.customer.util.OrigemCancelamento.CUSTOMER_SERVICE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    PedidoEventPublisher pedidoEventPublisher;

    @InjectMocks
    PedidoService pedidoService;

    @Test
    void deveCriarPedidoEPublicarEventoComSucesso() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        PedidoDtoRequest request = new PedidoDtoRequest(
                1L,
                1L,
                List.of(new ItemPedidoDtoRequest(100L, "Pastel de queijo", 2, 14.0))
        );

        Pedido pedidoSalvo = getPedido(pedidoId,clienteId, restauranteId, StatusPedido.CRIADO);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        PedidoDtoResponse response = pedidoService.criarPedidoEvent(request);

        verify(pedidoRepository).save(any(Pedido.class));
        verify(pedidoEventPublisher).publicarPedidoCriado(any(PedidoStatusEvent.class));

        assertThat(response.id()).isEqualTo(pedidoId);
        assertThat(response.clienteId()).isEqualTo(request.getIdCliente());
        assertThat(response.itens()).hasSize(1);
        assertThat(response.status()).isEqualTo(StatusPedido.CRIADO);
    }

    @Test
    void deveMarcarPedidoComoEntregueEPublicarEvento() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.EM_ROTA);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoStatusEvent resultado = pedidoService.informarPedidoEntregue(pedidoId);

        verify(pedidoRepository).save(pedido);
        verify(pedidoEventPublisher).publicarPedidoEntregue(any(PedidoStatusEvent.class));

        assertThat(resultado.getPedidoId()).isEqualTo(pedidoId);
        assertThat(resultado.getStatusPedido()).isEqualTo(StatusPedido.ENTREGUE);
    }

    @Test
    void deveLancarExcecaoSeStatusNaoForEmRota() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 2L;
        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.CRIADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(StatusIncorretoException.class,
                () -> pedidoService.informarPedidoEntregue(pedidoId));

        verify(pedidoRepository, never()).save(any());
        verify(pedidoEventPublisher, never()).publicarPedidoEntregue(any());
    }

    @Test
    void deveLancarExcecaoSePedidoNaoForEncontrado() {
        Long pedidoId = 3L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.informarPedidoEntregue(pedidoId));

        verify(pedidoRepository, never()).save(any());
        verify(pedidoEventPublisher, never()).publicarPedidoEntregue(any());
    }

    @Test
    void deveAtualizarPedidoComSucesso() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        Pedido pedidoExistente = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.PREPARANDO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoExistente));

        pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, dataAtualizacao);

        verify(pedidoRepository).save(pedidoExistente);
        assertThat(pedidoExistente.getStatusPedido()).isEqualTo(StatusPedido.EM_ROTA);
        assertThat(pedidoExistente.getDataAtualizacao()).isEqualTo(dataAtualizacao);
    }

    @Test
    void naoDeveAtualizarPedidoSeEstiverCancelado() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedidoCancelado = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.CANCELADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoCancelado));

        pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, LocalDateTime.now());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoSePedidoNaoForEncontradoAoAtualizar() {
        Long pedidoId = 999L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () ->
                pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, LocalDateTime.now()));
    }

    @Test
    void deveCancelarPedidoComSucesso() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.PREPARANDO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoCanceladoEvent evento = pedidoService.processarCancelamentoDePedido(pedidoId);

        verify(pedidoRepository).save(pedido);
        verify(pedidoEventPublisher).publicarPedidoCancelado(any(PedidoCanceladoEvent.class));

        assertThat(evento.getPedidoId()).isEqualTo(pedidoId);
        assertThat(evento.getStatusPedido()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(evento.getOrigemCancelamento()).isEqualTo(CUSTOMER_SERVICE.name());
        assertThat(evento.getMotivoCancelamento()).contains("cliente");
    }

    @Test
    void deveLancarExcecaoSePedidoJaEstiverCanceladoOuEmRota() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId, StatusPedido.EM_ROTA);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(StatusIncorretoException.class, () ->
                pedidoService.processarCancelamentoDePedido(pedidoId));

        verify(pedidoRepository, never()).save(any());
        verify(pedidoEventPublisher, never()).publicarPedidoCancelado(any());
    }

    @Test
    void deveLancarExcecaoSePedidoNaoForEncontradoAoCancelar() {
        Long pedidoId = 99L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () ->
                pedidoService.processarCancelamentoDePedido(pedidoId));

        verify(pedidoRepository, never()).save(any());
        verify(pedidoEventPublisher, never()).publicarPedidoCancelado(any());
    }
    private static Pedido getPedido(Long pedidoId, Long clienteId, Long restauranteId, StatusPedido status) {
        Long produtoId = 100L;
        Long itemPedidoId = 2L;

        Pedido pedido = new Pedido();

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(itemPedidoId);
        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(produtoId);
        itemPedido.setNomeProduto("Pastel de queijo");
        itemPedido.setPrecoUnitario(14.0);
        itemPedido.setQuantidade(1);

        pedido.setId(pedidoId);
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(restauranteId);
        pedido.setItens(List.of(itemPedido));
        pedido.setStatusPedido(status);
        return pedido;
    }

}
