package com.microservico.restaurant.services;

import com.microservico.restaurant.dto.request.RestaurantRequestDTO;
import com.microservico.restaurant.dto.response.RestaurantResponseDTO;
import com.microservico.restaurant.exceptions.RecursoNaoEncontradoException;
import com.microservico.restaurant.model.Restaurant;
import com.microservico.restaurant.repositories.RestaurantRepository;
import com.microservico.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RestaurantServiceTest {
    @Mock
    RestaurantRepository restaurantRepository;

    @InjectMocks
    RestaurantService restaurantService;

    @Captor
    ArgumentCaptor<Restaurant> restaurantArgumentCaptor;

    @Test
    void deveCadastrarNovoRestaurant() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO("Casa das Esfihas", "Avenida Brasil, 545", true);

        Restaurant restauranteSalvo = new Restaurant();
        restauranteSalvo.setId(1L);
        restauranteSalvo.setNome("Casa das Esfihas");
        restauranteSalvo.setEndereco("Avenida Brasil, 545");
        restauranteSalvo.setAtivo(true);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restauranteSalvo);

        RestaurantResponseDTO responseDTO = restaurantService.cadastrar(requestDTO);

        verify(restaurantRepository).save(restaurantArgumentCaptor.capture());
        Restaurant restaurantCapturado = restaurantArgumentCaptor.getValue();

        assertThat(restaurantCapturado.getNome()).isEqualTo("Casa das Esfihas");
        assertThat(restaurantCapturado.getEndereco()).isEqualTo("Avenida Brasil, 545");
        assertThat(restaurantCapturado.getAtivo()).isTrue();

        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.nome()).isEqualTo("Casa das Esfihas");

    }

    @Test
    void deveListarTodosOsRestaurantes() {
        List<Restaurant> mockList = getRestaurants();
        when(restaurantRepository.findAll()).thenReturn(mockList);

        List<RestaurantResponseDTO> resultado = restaurantService.listarTodos();

        verify(restaurantRepository).findAll();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("Casa das Esfihas");
        assertThat(resultado.get(1).nome()).isEqualTo("Pizza Legal");
    }

    @Test
    void deveRetornarRestaurantePorId() {
        Long restauranteId = 1L;

        Restaurant restaurant = getRestaurant(restauranteId);

        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO responseDTO = restaurantService.buscarPorId(restauranteId);

        assertThat(responseDTO.id()).isEqualTo(restauranteId);
        assertThat(responseDTO.nome()).isEqualTo("Casa dos Salgados");
        assertThat(responseDTO.endereco()).isEqualTo("Av. Cruzeiro do Sul, 123");
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoForEncontrado() {
        Long restauranteId = 99L;
        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> restaurantService.buscarPorId(restauranteId));
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        Long id = 1L;

        Restaurant restauranteExistente = new Restaurant();
        restauranteExistente.setId(id);
        restauranteExistente.setNome("Antigo Nome");
        restauranteExistente.setEndereco("Antigo Endereço");
        restauranteExistente.setAtivo(false);

        RestaurantRequestDTO dto = new RestaurantRequestDTO(
                "Novo Nome", "Novo Endereço", true
        );

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restauranteExistente));
        when(restaurantRepository.save(restauranteExistente)).thenReturn(restauranteExistente);

        RestaurantResponseDTO response = restaurantService.atualizar(dto, id);

        verify(restaurantRepository).save(restauranteExistente);
        assertThat(response.nome()).isEqualTo("Novo Nome");
        assertThat(response.endereco()).isEqualTo("Novo Endereço");
        assertThat(response.ativo()).isTrue();
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        Long idInexistente = 999L;
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Nome", "Endereço", true);

        when(restaurantRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> restaurantService.atualizar(dto, idInexistente));
    }

    @Test
    void deveExcluirItemQuandoExistir() {
        Long restauranteId = 1L;

        Restaurant restaurant = getRestaurant(restauranteId);

        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.of(restaurant));

        restaurantService.excluir(restauranteId);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoExistir() {
        Long restauranteId = 99L;

        when(restaurantRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> restaurantService.excluir(restauranteId));
    }

    private static List<Restaurant> getRestaurants() {
        Restaurant restaurante1 = new Restaurant();
        restaurante1.setId(1L);
        restaurante1.setNome("Casa das Esfihas");
        restaurante1.setEndereco("Av. Brasil, 123");
        restaurante1.setAtivo(true);

        Restaurant restaurante2 = new Restaurant();
        restaurante2.setId(2L);
        restaurante2.setNome("Pizza Legal");
        restaurante2.setEndereco("Rua Central, 987");
        restaurante2.setAtivo(false);

        return List.of(restaurante1, restaurante2);
    }

    private static Restaurant getRestaurant(Long restauranteId) {
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restauranteId);
        restaurante.setNome("Casa dos Salgados");
        restaurante.setEndereco("Av. Cruzeiro do Sul, 123");
        restaurante.setAtivo(true);

        return restaurante;
    }
}
