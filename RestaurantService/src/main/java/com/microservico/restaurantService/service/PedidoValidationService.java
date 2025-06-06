package com.microservico.restaurantService.service;

import com.microservico.restaurantService.event.PedidoCriadoEvent;
import com.microservico.restaurantService.model.MenuItem;
import com.microservico.restaurantService.model.Restaurant;
import com.microservico.restaurantService.repositories.MenuItemRepository;
import com.microservico.restaurantService.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoValidationService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public boolean validarRestautanteEItens(Long idRestaurant, List<PedidoCriadoEvent.ItemPedidoEvent> itens) {

        Restaurant restaurant = restaurantRepository.findById(idRestaurant).orElse(null);

        if (Objects.equals(restaurant, null)) {
            log.warn("Restaurant {} não encontrado!", idRestaurant);
            return false;
        }

        if (!restaurant.isAtivo()) {
            log.warn("Restaurante {} está inativado!", idRestaurant);
            return false;
        }

        boolean algumItemInvalido = itens.stream().anyMatch(item -> {
            Optional<MenuItem> menuItem = menuItemRepository.findById(item.getProdutoId());

            if (menuItem.isEmpty()) {
                log.warn("Item {} não encontrado!", item.getProdutoId());
                return true;
            }

            if (!menuItem.get().getAtivo()) {
                log.warn("Item {} está inativo", item.getProdutoId());
                return true;
            }
            return false;
        });

        return !algumItemInvalido;
    }


}
