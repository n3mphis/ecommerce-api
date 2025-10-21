package com.solarix_api.ecommerce_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    // Nombre del producto
    private String name;

    @Column(nullable = false)
    // Precio del producto
    private BigDecimal price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(unique = true, nullable = false)
    // Cantidad disponible en inventario
    private int stock;

    public Product(String name, BigDecimal price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
