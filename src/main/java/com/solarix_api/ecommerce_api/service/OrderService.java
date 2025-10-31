package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.CheckoutItemResponse;
import com.solarix_api.ecommerce_api.dto.CheckoutResponse;
import com.solarix_api.ecommerce_api.exception.*;
import com.solarix_api.ecommerce_api.model.*;
import com.solarix_api.ecommerce_api.repository.CartRepository;
import com.solarix_api.ecommerce_api.repository.OrderRepository;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CheckoutResponse checkout(String email) {
        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new CarritoNoEncontradoException("No se encontró el carrito"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ProductoNoEncontradoException("El producto no fue encontrado"));

            if (cartItem.getQuantity() > product.getStock()) {
                throw new StockInsuficienteException("No hay suficiente stock");
            }

            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), product.getPrice());

            orderItems.add(orderItem);

            BigDecimal subtotal = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(subtotal);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        Order order = new Order(cart.getUser(), total, Estado.PENDIENTE, orderItems);

        cart.getItems().clear();
        cartRepository.save(cart);

        try {
            log.info("Procesando pago del pedido #{}...", order.getId());
            Thread.sleep(1500);
            order.setEstado(Estado.COMPLETADO);
            log.info("Pago aprobado. Pedido #{} pagado exitosamente.", order.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            order.setEstado(Estado.CANCELADO);
            throw new PagoFallidoException("Error procesando el pago. Intente nuevamente");
        }

        Order ordenGuardada = orderRepository.save(order);

        List<CheckoutItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem oi : ordenGuardada.getItems()) {
            BigDecimal subtotal = BigDecimal.valueOf(oi.getPrecio())
                    .multiply(BigDecimal.valueOf(oi.getCantidad()));

            CheckoutItemResponse itemDto = new CheckoutItemResponse(
                    oi.getProduct().getName(),
                    oi.getCantidad(),
                    BigDecimal.valueOf(oi.getPrecio()),
                    subtotal
            );

            itemResponses.add(itemDto);
        }
        CheckoutResponse response = new CheckoutResponse(
                ordenGuardada.getId(),
                ordenGuardada.getTotal(),
                ordenGuardada.getEstado(),
                LocalDateTime.now(),
                "Checkout exitoso. Tu pedido ha sido creado."
        );

        return response;
    }
}
