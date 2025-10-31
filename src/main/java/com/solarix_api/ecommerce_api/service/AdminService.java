package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.ProductResponse;
import com.solarix_api.ecommerce_api.dto.ProductUpdateRequest;
import com.solarix_api.ecommerce_api.dto.ProductoCreateRequest;
import com.solarix_api.ecommerce_api.dto.UsersResponse;
import com.solarix_api.ecommerce_api.exception.ProductoNoEncontradoException;
import com.solarix_api.ecommerce_api.exception.SinProductosAgregados;
import com.solarix_api.ecommerce_api.exception.UsuarioNoEncontradoException;
import com.solarix_api.ecommerce_api.model.Product;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.OrderRepository;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ProductResponse actualizarProducto(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con id: " +
                        id + " no fue encontrado"));

        if (request.productName() != null) {
            product.setName(request.productName());
        }
        if (request.price() != null && request.price() > 0) {
            product.setPrice(request.price());
        }
        if (request.stock() != null && request.stock() > 0) {
            product.setStock(request.stock());
        }

        Product productoGuardado = productRepository.save(product);

        return new ProductResponse(productoGuardado.getId(),
                productoGuardado.getName(),
                productoGuardado.getPrice(),
                productoGuardado.getStock());
    }

    public void eliminarProducto(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con id: "
                + id + " no fue encontrado"));

        productRepository.delete(product);
    }

    @Transactional
    public ProductResponse crearNuevoProducto(ProductoCreateRequest request) {
        if (request.name() == null || request.name().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (request.price() == null || request.price() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (request.stock() == null || request.stock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor a 0");
        }

        Product product = new Product(request.name(), request.price(), request.stock());

        Product productoGuardado = productRepository.save(product);

        return new ProductResponse(productoGuardado.getId(),
                productoGuardado.getName(),
                productoGuardado.getPrice(),
                productoGuardado.getStock());
    }
}
