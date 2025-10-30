package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.exception.CarritoVacioException;
import com.solarix_api.ecommerce_api.exception.EmailNoEncontradoException;
import com.solarix_api.ecommerce_api.exception.ProductoNoEncontradoException;
import com.solarix_api.ecommerce_api.model.Cart;
import com.solarix_api.ecommerce_api.model.CartItem;
import com.solarix_api.ecommerce_api.model.Product;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.CartItemRepository;
import com.solarix_api.ecommerce_api.repository.CartRepository;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository itemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, CartItemRepository itemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public Cart agregarProducto(String email, Long productId, int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNoEncontradoException("El email: " + email + " no fue encontrado!"));

        Cart cart = cartRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID: " + productId + " no fue encontrado"));

        CartItem itemExistente = itemRepository.findByCartAndProductId(cart, productId)
                .orElse(null);

        CartItem cartItem;
        if (itemExistente != null) {
            itemExistente.setQuantity(itemExistente.getQuantity() + quantity);
            cartItem = itemRepository.save(itemExistente);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem = itemRepository.save(cartItem);
        }

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }
        if (!cart.getItems().contains(cartItem)) {
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart retornarContenidoDelCarrito(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new EmailNoEncontradoException("El email: " + email + " no fue encontrado"));
    }
}
