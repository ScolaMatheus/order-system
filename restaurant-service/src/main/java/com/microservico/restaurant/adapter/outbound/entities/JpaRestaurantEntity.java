package com.microservico.restaurant.adapter.outbound.entities;

import com.microservico.restaurant.model.MenuItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaRestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaMenuItemEntity> cardapio;

}
