package com.solarix_api.ecommerce_api.dto;

public record ProductUpdateRequest(Long id,
                                   String productName,
                                   Double price,
                                   Integer stock) {
}
