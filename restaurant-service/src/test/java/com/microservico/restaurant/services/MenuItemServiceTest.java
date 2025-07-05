package com.microservico.restaurant.services;

import com.microservico.restaurant.dto.request.MenuItemRequestDTO;
import com.microservico.restaurant.dto.response.MenuItemResponseDTO;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.model.MenuItem;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.repositories.MenuItemRepository;
import com.microservico.restaurant.repositories.RestaurantRepository;
import com.microservico.restaurant.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MenuItemServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    MenuItemRepository menuItemRepository;

    @InjectMocks
    MenuItemService menuItemService;

    @Captor
    ArgumentCaptor<MenuItem> itemArgumentCaptor;

    @Test
    void deveCadastrarNovoItem() {
        Long restaurantId = 1L;
        Long itemId = 1L;

        Restaurant restaurant = getRestaurant(restaurantId);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        MenuItem menuItemSalvo = getMenuItem(itemId, restaurant);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItemSalvo);

        MenuItemRequestDTO requestDTO = getMenuRequest(menuItemSalvo);

        MenuItemResponseDTO responseDTO = menuItemService.cadastrar(requestDTO);

        verify(menuItemRepository).save(itemArgumentCaptor.capture());
        MenuItem capturado = itemArgumentCaptor.getValue();

        assertThat(capturado.getNome()).isEqualTo("Pastel de queijo");
        assertThat(capturado.getPreco()).isEqualTo(BigDecimal.valueOf(14.0));
        assertThat(capturado.getRestaurant()).isEqualTo(restaurant);

        assertThat(responseDTO.getId()).isEqualTo(itemId);
        assertThat(responseDTO.getNome()).isEqualTo("Pastel de queijo");

    }

    @Test
    void deveListarMenuPorRestaurante() {
        Long restaurantId = 1L;
        Long itemId = 1L;

        Restaurant restaurant = getRestaurant(restaurantId);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        MenuItem menuItem = getMenuItem(itemId, restaurant);
        when(menuItemRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(menuItem));

        List<MenuItemResponseDTO> cardapio = menuItemService.listarPorRestaurante(restaurantId);

        verify(menuItemRepository).findByRestaurantId(restaurantId);
        assertThat(cardapio).hasSize(1);
        assertThat(cardapio.get(0).getId()).isEqualTo(itemId);
        assertThat(cardapio.get(0).getNome()).isEqualTo("Pastel de queijo");

    }

    @Test
    void deveRetornarMenuPorId() {
        Long itemId = 1L;

        Restaurant restaurante = getRestaurant(1L);
        MenuItem menuItem = getMenuItem(itemId, restaurante);

        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));

        MenuItemResponseDTO response = menuItemService.buscarPorId(itemId);

        assertThat(response.getId()).isEqualTo(itemId);
        assertThat(response.getNome()).isEqualTo("Pastel de queijo");
        assertThat(response.getPreco()).isEqualTo(BigDecimal.valueOf(14.0));
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoForEncontrado() {
        Long itemId = 99L;
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> menuItemService.buscarPorId(itemId));
    }

    @Test
    void deveAtualizarTodosOsCamposDoItem() {
        Long itemId = 1L;
        Long restaurantId = 1L;
        Long novoRestauranteId = 2L;

        Restaurant restauranteAtual = getRestaurant(restaurantId);
        Restaurant novoRestaurante = getRestaurant(novoRestauranteId);

        MenuItem itemExistente = getMenuItem(itemId, restauranteAtual);
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(itemExistente));
        when(restaurantRepository.findById(novoRestauranteId)).thenReturn(Optional.of(novoRestaurante));

        MenuItemRequestDTO dto = new MenuItemRequestDTO("Coxinha Vegana", BigDecimal.valueOf(12), novoRestauranteId, true);

        MenuItemResponseDTO resultado = menuItemService.atualizar(dto, itemId);

        verify(menuItemRepository).save(itemExistente);
        assertThat(resultado.getNome()).isEqualTo("Coxinha Vegana");
        assertThat(resultado.getPreco()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(resultado.getAtivo()).isTrue();
        assertThat(resultado.getRestaurantId()).isEqualTo(novoRestauranteId);
    }

    @Test
    void deveAtualizarSomenteNomeEPrecoQuandoOutrosCamposForemNull() {
        Long itemId = 1L;

        Restaurant restaurante = getRestaurant(1L);
        MenuItem item = getMenuItem(itemId, restaurante);

        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        MenuItemRequestDTO dto = new MenuItemRequestDTO(
                "Nova Coxinha", BigDecimal.valueOf(9.5), null, null
        );

        MenuItemResponseDTO resultado = menuItemService.atualizar(dto, itemId);

        verify(menuItemRepository).save(item);
        assertThat(resultado.getNome()).isEqualTo("Nova Coxinha");
        assertThat(resultado.getPreco()).isEqualTo(BigDecimal.valueOf(9.5));
        assertThat(resultado.getAtivo()).isEqualTo(item.getAtivo());
        assertThat(resultado.getRestaurantId()).isEqualTo(item.getRestaurant().getId());
    }

    @Test
    void deveExcluirItemQuandoExistir() {
        Long itemId = 1L;
        Restaurant restaurante = getRestaurant(1L);
        MenuItem menuItem = getMenuItem(itemId, restaurante);

        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));

        menuItemService.excluir(itemId);

        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoExistir() {
        Long itemId = 999L;

        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> menuItemService.excluir(itemId));
    }

    private static Restaurant getRestaurant(Long restauranteId) {
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restauranteId);
        restaurante.setNome("Casa dos Salgados");
        restaurante.setAtivo(true);

        return restaurante;
    }

    private MenuItemRequestDTO getMenuRequest(MenuItem menuItem) {
        return new MenuItemRequestDTO(
                menuItem.getNome(),
                menuItem.getPreco(),
                menuItem.getRestaurant().getId(),
                menuItem.getAtivo()
        );
    }

    private static MenuItem getMenuItem(Long itemId, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();

        menuItem.setId(itemId);
        menuItem.setNome("Pastel de queijo");
        menuItem.setPreco(BigDecimal.valueOf(14.0));
        menuItem.setRestaurant(restaurant);
        menuItem.setAtivo(true);

        return menuItem;
    }
}
