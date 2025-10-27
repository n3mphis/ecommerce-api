package com.solarix_api.ecommerce_api.repository;

import com.solarix_api.ecommerce_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
