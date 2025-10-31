package com.solarix_api.ecommerce_api.dto;

import com.solarix_api.ecommerce_api.model.Cart;
import com.solarix_api.ecommerce_api.model.Role;

public record UsersResponse(Long id, String email, Role role, Long cartId) {
}
