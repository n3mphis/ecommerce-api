package com.solarix_api.ecommerce_api.dto;

public record ProductResponse(Long productId, String productName, double price, int stock) {
}
