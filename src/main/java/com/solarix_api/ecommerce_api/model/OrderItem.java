package com.solarix_api.ecommerce_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "items_pedido")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    // Pedido al que pertenece este item
    private Order order;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    // Producto del pedido
    private Product product;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    // Cantidad del producto
    private int cantidad;

    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    // Precio unitario al momento del pedido
    private double precio;

    public OrderItem(Product product, int cantidad, double precio) {
        this.product = product;
        this.cantidad = cantidad;
        this.precio = precio;
    }
}
