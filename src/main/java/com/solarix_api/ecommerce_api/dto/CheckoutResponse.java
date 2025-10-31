package com.solarix_api.ecommerce_api.dto;

import com.solarix_api.ecommerce_api.model.Estado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CheckoutResponse(Long orderId, BigDecimal total, Estado estado,
                               LocalDateTime fecha, String mensaje) {
}
