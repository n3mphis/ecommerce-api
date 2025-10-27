package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.CartItemResponse;
import com.solarix_api.ecommerce_api.dto.CartResponse;
import com.solarix_api.ecommerce_api.dto.ProductoRequest;
import com.solarix_api.ecommerce_api.exception.EmailNoEncontradoException;
import com.solarix_api.ecommerce_api.exception.ProductoNoEncontradoException;
import com.solarix_api.ecommerce_api.model.Cart;
import com.solarix_api.ecommerce_api.model.Product;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public CartController(CartService cartService, ProductRepository productRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
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
                            .map(item -> {
                                Product product = productRepository.findById(item.getProductId())
                                        .orElseThrow(() -> new ProductoNoEncontradoException("Producto no fue encontrado:" + item.getProductId()));

                                return new CartItemResponse(
                                        item.getProductId(),
                                        product.getName(),
                                        item.getQuantity(),
                                        product.getPrice()
                                );
                            })
                            .collect(Collectors.toList()),
                    cart.getItems().stream()
                            .mapToDouble(item -> {
                                Product product = productRepository.findById(item.getProductId())
                                        .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + item.getProductId()));
                                return product.getPrice() * item.getQuantity();
                            })
                            .sum()
            );
            return ResponseEntity.ok(response);

        } catch (EmailNoEncontradoException e) {
            throw new EmailNoEncontradoException("El email: " + email + " no fue encontrado");
        }
    }
}
