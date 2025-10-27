package com.solarix_api.ecommerce_api.repository;

import com.solarix_api.ecommerce_api.model.Cart;
import com.solarix_api.ecommerce_api.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
}
