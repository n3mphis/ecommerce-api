package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.ProductResponse;
import com.solarix_api.ecommerce_api.dto.UsersResponse;
import com.solarix_api.ecommerce_api.exception.SinProductosAgregados;
import com.solarix_api.ecommerce_api.exception.UsuarioNoEncontradoException;
import com.solarix_api.ecommerce_api.model.Product;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.OrderRepository;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AdminService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<UsersResponse> listarTodosLosUsuarios() {
        List<User> usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new UsuarioNoEncontradoException("No hay usuarios registrados.");
        }

        List<UsersResponse> responseList = new ArrayList<>();
        for (User user : usuarios) {
            Long cartId = user.getCart() != null ? user.getCart().getId() : null;
            UsersResponse response = new UsersResponse(user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    cartId);

            responseList.add(response);
        }
        return responseList;
    }

    public List<ProductResponse> listarTodosLosProductos() {
        List<Product> productos = productRepository.findAll();

        if (productos.isEmpty()) {
            throw new SinProductosAgregados("No hay productos agregados");
        }

        List<ProductResponse> productsResponse = new ArrayList<>();
        for (Product product : productos) {
            ProductResponse response = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock()
            );

            productsResponse.add(response);
        }

        return productsResponse;
    }
}
