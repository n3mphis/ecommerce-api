package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.CartItemResponse;
import com.solarix_api.ecommerce_api.dto.CartResponse;
import com.solarix_api.ecommerce_api.dto.ProductoRequest;
import com.solarix_api.ecommerce_api.exception.EmailNoEncontradoException;
import com.solarix_api.ecommerce_api.model.Cart;
import com.solarix_api.ecommerce_api.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartResponse> agregarProducto(@RequestBody ProductoRequest productoRequest) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (productoRequest == null || productoRequest.productoId() == null ||
        productoRequest.quantity() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Cart cart = cartService.agregarProducto(email, productoRequest.productoId(), productoRequest.quantity());
            CartResponse response = new CartResponse(
                    cart.getId(),
                    cart.getItems().stream()
                            .map(item -> new CartItemResponse(item.getProductId(),
                                    item.getQuantity()))
                            .collect(Collectors.toList()));

            return ResponseEntity.ok(response);

        } catch (EmailNoEncontradoException e) {
            throw new EmailNoEncontradoException("El email: " + email + " no fue encontrado");
        }
    }
}
