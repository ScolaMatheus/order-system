package com.microservico.restaurant.adapter.outbound.entities;

import com.microservico.restaurant.model.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_menu_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaMenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    private JpaRestaurantEntity restaurant;

    @Column(nullable = false)
    private Boolean ativo;

}
