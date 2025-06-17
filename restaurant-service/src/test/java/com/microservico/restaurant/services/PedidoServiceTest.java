package com.microservico.restaurant.services;

import com.microservico.restaurant.dto.response.PedidoDtoResponse;
import com.microservico.restaurant.event.PedidoCanceladoEvent;
import com.microservico.restaurant.event.PedidoStatusEvent;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.exceptions.StatusIncorretoException;
import com.microservico.restaurant.model.ItemPedido;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Pedido;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.publisher.PedidoEventPublisher;
import com.microservico.restaurant.repositories.MenuItemRepository;
import com.microservico.restaurant.repositories.PedidoRepository;
import com.microservico.restaurant.repositories.RestaurantRepository;
import com.microservico.restaurant.service.PedidoService;
import com.microservico.restaurant.util.StatusPedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PedidoServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    MenuItemRepository menuItemRepository;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    PedidoEventPublisher pedidoEventPublisher;

    @InjectMocks
    PedidoService pedidoService;

    @Captor
    ArgumentCaptor<PedidoStatusEvent> eventArgumentCaptor;

    @Captor
    ArgumentCaptor<PedidoCanceladoEvent> canceladoEventArgumentCaptor;

    @Captor
    ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    void deveProcessarPedidoQuandoRestauranteEItensValidos() {

        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;
        Long produtoId = 100L;

        PedidoStatusEvent event = getStatusEvent(produtoId, pedidoId, clienteId, restauranteId);

        Restaurant restaurante = getRestaurant(restauranteId, true);
        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));

        // Item mockado
        MenuItem item = new MenuItem();
        item.setId(produtoId);
        item.setNome("Pizza");
        item.setPreco(14.0);
        item.setAtivo(true);
        when(menuItemRepository.findById(produtoId)).thenReturn(Optional.of(item));

        pedidoService.consumirPedido(event);

        verify(pedidoRepository).save(pedidoCaptor.capture());
        verify(pedidoEventPublisher).publicarStatusPedido(eventArgumentCaptor.capture());

        Pedido pedidoSalvo = pedidoCaptor.getValue();
        PedidoStatusEvent eventoPublicado = eventArgumentCaptor.getValue();

        assertThat(pedidoSalvo.getStatusPedido()).isEqualTo(StatusPedido.PREPARANDO);
        assertThat(eventoPublicado.getStatusPedido()).isEqualTo(StatusPedido.PREPARANDO);
    }

    @Test
    void deveCancelarPedidoQuandoRestauranteForInvalido() {

        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;
        Long produtoId = 100L;

        PedidoStatusEvent event = getStatusEvent(produtoId, pedidoId, clienteId, restauranteId);

        Restaurant restaurant = getRestaurant(restauranteId, false);
        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.of(restaurant));

        pedidoService.consumirPedido(event);

        verify(pedidoRepository).save(pedidoCaptor.capture());
        verify(pedidoEventPublisher, org.mockito.Mockito.never()).publicarStatusPedido(any());
        verify(pedidoEventPublisher).publicarPedidoCancelado(canceladoEventArgumentCaptor.capture());

        Pedido pedidoSalvo = pedidoCaptor.getValue();
        PedidoCanceladoEvent eventoPublicado = canceladoEventArgumentCaptor.getValue();

        assertThat(pedidoSalvo.getStatusPedido()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(eventoPublicado.getStatusPedido()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    void deveCancelarPedidoQuandoUmDosItensForInvalido() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Long produtoValido = 100L;
        Long produtoInvalido = 200L;

        PedidoStatusEvent.ItemPedidoEvent item1 = new PedidoStatusEvent.ItemPedidoEvent(produtoValido, "Pizza", 1, 20.0);
        PedidoStatusEvent.ItemPedidoEvent item2 = new PedidoStatusEvent.ItemPedidoEvent(produtoInvalido, "Refrigerante", 1, 5.0);

        PedidoStatusEvent event = new PedidoStatusEvent();
        event.setPedidoId(pedidoId);
        event.setClienteId(clienteId);
        event.setRestauranteId(restauranteId);
        event.setItens(List.of(item1, item2));

        Restaurant restaurante = getRestaurant(restauranteId, true);
        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));

        MenuItem itemValido = new MenuItem();
        itemValido.setId(produtoValido);
        itemValido.setAtivo(true);
        when(menuItemRepository.findById(produtoValido)).thenReturn(Optional.of(itemValido));

        MenuItem itemInvalido = new MenuItem();
        itemInvalido.setId(produtoInvalido);
        itemInvalido.setAtivo(false);
        when(menuItemRepository.findById(produtoInvalido)).thenReturn(Optional.of(itemInvalido));

        pedidoService.consumirPedido(event);

        verify(pedidoRepository).save(pedidoCaptor.capture());
        verify(pedidoEventPublisher, org.mockito.Mockito.never()).publicarStatusPedido(any());
        verify(pedidoEventPublisher).publicarPedidoCancelado(canceladoEventArgumentCaptor.capture());

        assertThat(pedidoCaptor.getValue().getStatusPedido()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    void deveAtualizarPedidoParaEmRotaQuandoStatusForPreparando() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;

        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        PedidoStatusEvent resultado = pedidoService.informarPedidoPronto(pedidoId);

        verify(pedidoRepository).save(pedido);
        verify(pedidoEventPublisher).publicarStatusPedido(resultado);

        assertThat(resultado.getStatusPedido()).isEqualTo(StatusPedido.EM_ROTA);
    }

    @Test
    void deveLancarExcecaoQuandoStatusNaoForPreparando() {
        Long pedidoId = 2L;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(StatusPedido.CRIADO);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(StatusIncorretoException.class,
                () -> pedidoService.informarPedidoPronto(pedidoId));
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoExistir() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.informarPedidoPronto(99L));
    }

    @Test
    void deveRetornarListaDePedidosDtoPorRestaurante() {
        Long restauranteId = 1L;
        Long clienteId = 1L;
        Long pedidoId = 10L;
        Pedido pedido = getPedido(pedidoId, clienteId, restauranteId);

        when(pedidoRepository.findByRestauranteId(restauranteId))
                .thenReturn(List.of(pedido));

        List<PedidoDtoResponse> pedidos = pedidoService.buscarPedidoPorRestaurante(restauranteId);

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getRestaurantId()).isEqualTo(restauranteId);
    }

    @Test
    void deveAtualizarStatusEPersistirQuandoPedidoValido() {
        Long pedidoId = 3L;
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(StatusPedido.CRIADO);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        pedidoService.atualizarPedido(pedidoId, StatusPedido.EM_ROTA, dataAtualizacao);

        verify(pedidoRepository).save(pedido);
        assertThat(pedido.getStatusPedido()).isEqualTo(StatusPedido.EM_ROTA);
        assertThat(pedido.getDataAtualizacao()).isEqualTo(dataAtualizacao);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarPedidoInexistente() {
        when(pedidoRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.atualizarPedido(404L, StatusPedido.EM_ROTA, LocalDateTime.now()));
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
        itemPedido.setPrecoUnitario(14.0);
        itemPedido.setQuantidade(1);

        pedido.setId(pedidoId);
        pedido.setClienteId(clienteId);
        pedido.setRestauranteId(restauranteId);
        pedido.setItens(List.of(itemPedido));
        pedido.setStatusPedido(StatusPedido.PREPARANDO);
        return pedido;
    }

    private static PedidoStatusEvent getStatusEvent(Long produtoId, Long pedidoId, Long clienteId, Long restauranteId) {

        PedidoStatusEvent.ItemPedidoEvent itemPedido = new PedidoStatusEvent.ItemPedidoEvent(
                produtoId,
                "Pastel de queijo",
                2,
                14.0
        );

        PedidoStatusEvent event = new PedidoStatusEvent();
        event.setPedidoId(pedidoId);
        event.setClienteId(clienteId);
        event.setRestauranteId(restauranteId);
        event.setItens(List.of(itemPedido));

        return event;
    }

    private static Restaurant getRestaurant(Long restauranteId, Boolean ativo) {
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restauranteId);
        restaurante.setNome("Casa dos Salgados");
        restaurante.setAtivo(ativo);

        return restaurante;
    }
}
