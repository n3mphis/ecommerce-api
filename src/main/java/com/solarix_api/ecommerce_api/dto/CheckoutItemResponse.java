package com.solarix_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record CheckoutItemResponse(String productName, int quantity, BigDecimal precioUnidad,
                                   BigDecimal subtotal) {
}
