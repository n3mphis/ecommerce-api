package com.solarix_api.ecommerce_api.config;

import com.solarix_api.ecommerce_api.model.Product;
import com.solarix_api.ecommerce_api.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDataBase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product("Pan", 1.50, 10));
                productRepository.save(new Product("Manteca", 2.00, 20));
                productRepository.save(new Product("Fideos", 1.20, 10));
                productRepository.save(new Product("Galletitas", 1.65, 10));
                productRepository.save(new Product("Pizza", 1.70, 10));
                productRepository.save(new Product("Queso", 1.20, 10));
                System.out.println("Productos inicializados: Pan, Manteca, Fideos, Galletitas, Pizza, Queso");
            }
        };
    }
}
