package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.CartItemResponse;
import com.solarix_api.ecommerce_api.dto.CartResponse;
import com.solarix_api.ecommerce_api.dto.CheckoutResponse;
import com.solarix_api.ecommerce_api.dto.ProductoRequest;
import com.solarix_api.ecommerce_api.exception.*;
import com.solarix_api.ecommerce_api.model.*;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import com.solarix_api.ecommerce_api.service.CartService;
import com.solarix_api.ecommerce_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartController(CartService cartService, ProductRepository productRepository,
                          OrderService orderService) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
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
                                        .orElseThrow(() ->
                                                new ProductoNoEncontradoException("Producto no fue encontrado:"
                                                        + item.getProductId()));

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
                                        .orElseThrow(() ->
                                                new ProductoNoEncontradoException("Producto no encontrado: "
                                                        + item.getProductId()));
                                return product.getPrice() * item.getQuantity();
                            })
                            .sum()
            );
            return ResponseEntity.ok(response);

        } catch (EmailNoEncontradoException e) {
            throw new EmailNoEncontradoException("El email: " + email + " no fue encontrado");
        }
    }

    @GetMapping("/me")
    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<CartResponse> verCarrito() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Cart cart = cartService.retornarContenidoDelCarrito(email);
            CartResponse response = new CartResponse(
                    cart.getId(),
                    cart.getItems().stream()
                            .map(item -> {
                                Product product = productRepository.findById(item.getProductId())
                                        .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado " + item.getProductId()));
                                return new CartItemResponse(product.getId(),
                                        product.getName(),
                                        product.getStock(),
                                        product.getPrice());
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
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/checkout")
    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<CheckoutResponse> checkout() throws InterruptedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CheckoutResponse response = orderService.checkout(email);
        return ResponseEntity.ok(response);
    }
}
