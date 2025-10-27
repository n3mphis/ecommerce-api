package com.solarix_api.ecommerce_api.dto;

public record CartItemResponse(Long productId, String productName, int quantity, double price) {
}
