package com.solarix_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    // Usuario que realizó el pedido
    private User user;

    @Column(nullable = false)
    // Total del pedido
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // Estado del pedido
    private Estado estado;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    // Fecha de creación
    private LocalDateTime createdAt;

    public Order(User user, BigDecimal total, Estado estado) {
        this.user = user;
        this.total = total;
        this.estado = estado;
    }
}
