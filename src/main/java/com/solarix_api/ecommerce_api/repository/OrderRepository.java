package com.solarix_api.ecommerce_api.repository;

import com.solarix_api.ecommerce_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
