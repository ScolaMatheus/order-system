package com.microservico.order.service;

import com.microservico.order.dto.response.PedidoDtoResponse;
import com.microservico.order.event.PedidoStatusEvent;
import com.microservico.order.exceptions.RecursoNaoEncontradoException;
import com.microservico.order.model.ItemPedido;
import com.microservico.order.model.Pedido;
import com.microservico.order.util.StatusPedido;
import com.microservico.order.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PedidoServiceTest {
    
    @Mock
    PedidoRepository pedidoRepository;

    @InjectMocks
    PedidoService pedidoService;

    @Captor
    ArgumentCaptor<Pedido> pedidoArgumentCaptor;
    
    
    @Test
    void deveCriarNovoPedido() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        PedidoStatusEvent event = getEvent(pedidoId, clienteId, restauranteId);

        pedidoService.criarPedido(event);

        verify(pedidoRepository).save(pedidoArgumentCaptor.capture());
        Pedido pedidoCapturado = pedidoArgumentCaptor.getValue();
        
        assertThat(pedidoCapturado.getId()).isEqualTo(pedidoId);
        assertThat(pedidoCapturado.getClienteId()).isEqualTo(clienteId);
        assertThat(pedidoCapturado.getRestauranteId()).isEqualTo(restauranteId);
        assertThat(pedidoCapturado.getStatusPedido()).isEqualTo(StatusPedido.CRIADO);
    }

    @Test
    void deveListarTodosOsPedidos() {
        List<Pedido> mockList = getPedidos();
        when(pedidoRepository.findAll()).thenReturn(mockList);

        List<PedidoDtoResponse> resultado = pedidoService.buscarPedidos();

        verify(pedidoRepository).findAll();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(1L);
        assertThat(resultado.get(0).getItens()).hasSize(1);
        assertThat(resultado.get(0).getValorTotal()).isEqualTo(mockList.get(0).getItens().get(0).getValorTotal());
        assertThat(resultado.get(0).getStatus()).isEqualTo(StatusPedido.CRIADO);

    }

    @Test
    void deveRetornarPedidoPorId() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        PedidoDtoResponse responseDto = pedidoService.buscarPedidoPorId(pedidoId);

        assertThat(responseDto.getId()).isEqualTo(pedidoId);
        assertThat(responseDto.getValorTotal()).isEqualTo(pedido.getValorTotal());
        assertThat(responseDto.getItens()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() {
        Long pedidoId = 99L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.buscarPedidoPorId(pedidoId));
    }

    @Test
    void deveAtualizarPedidoComSucesso() {
        Long pedidoId = 10L;
        StatusPedido novoStatus = StatusPedido.EM_ROTA;
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        Pedido pedidoExistente = getPedido(pedidoId, 1L, 1L);
        pedidoExistente.setStatusPedido(StatusPedido.PREPARANDO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoExistente));

        pedidoService.atualizarPedido(pedidoId, novoStatus, dataAtualizacao);

        verify(pedidoRepository).save(pedidoExistente);
        assertThat(pedidoExistente.getStatusPedido()).isEqualTo(novoStatus);
        assertThat(pedidoExistente.getDataAtualizacao()).isEqualTo(dataAtualizacao);
    }

    @Test
    void naoDeveAtualizarPedidoSeJaEstiverCancelado() {
        Long pedidoId = 10L;
        Long clienteId = 1L;
        Long restauranteId = 1L;

        Pedido pedidoCancelado = getPedido(pedidoId, clienteId, restauranteId);
        pedidoCancelado.setStatusPedido(StatusPedido.CANCELADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoCancelado));

        pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, LocalDateTime.now());

        // Verifica que o save não foi chamado
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoSePedidoNaoForEncontradoAoAtualizarPedido() {
        Long pedidoId = 99L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, LocalDateTime.now()));
    }

    @Test
    void deveExcluirPedidoQuandoExistir() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        pedidoService.excluirPedido(pedidoId);

        verify(pedidoRepository).delete(pedido);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoExistirAoDeletar() {
        long pedidoId = 99L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.excluirPedido(pedidoId));
    }

    private static PedidoStatusEvent getEvent(Long pedidoId, Long clienteId, Long restauranteId) {
        Long produtoId = 100L;

        PedidoStatusEvent.ItemPedidoEvent itemPedido = new PedidoStatusEvent.ItemPedidoEvent(
                produtoId,
                "Pastel de queijo",
                2,
                BigDecimal.valueOf(14)
        );

        PedidoStatusEvent event = new PedidoStatusEvent();
        event.setPedidoId(pedidoId);
        event.setClienteId(clienteId);
        event.setRestauranteId(restauranteId);
        event.setStatusPedido(StatusPedido.CRIADO);
        event.setItens(List.of(itemPedido));
        return event;
    }

    private static List<Pedido> getPedidos() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long produtoId = 10L;

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(restauranteId);

        ItemPedido item = new ItemPedido(1L, pedido, produtoId, "Pastel de queijo", BigDecimal.valueOf(14), 2);

        pedido.setItens(List.of(item));
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatusPedido(StatusPedido.CRIADO);
        pedido.setValorTotal(item.getValorTotal());

        return List.of(pedido);
    }

    private static Pedido getPedido(Long pedidoId, Long clienteId, Long restauranteId) {
        Long produtoId = 100L;
        Long itemPedidoId = 2L;

        Pedido pedido = new Pedido();

        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setId(itemPedidoId);
        itemPedido.setPedido(pedido);
        itemPedido.setProdutoId(produtoId);
        itemPedido.setNomeProduto("Pastel de queijo");
        itemPedido.setPrecoUnitario(BigDecimal.valueOf(14));
        itemPedido.setQuantidade(1);

        pedido.setId(pedidoId);
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(restauranteId);
        pedido.setItens(List.of(itemPedido));
        pedido.setStatusPedido(StatusPedido.PREPARANDO);
        pedido.setValorTotal(itemPedido.getValorTotal());
        return pedido;
    }
}
