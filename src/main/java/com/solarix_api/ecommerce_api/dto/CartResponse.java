package com.solarix_api.ecommerce_api.dto;

import java.util.List;

public record CartResponse(Long cartId, List<CartItemResponse> items) {
}
