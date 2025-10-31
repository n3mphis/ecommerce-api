package com.solarix_api.ecommerce_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductoCreateRequest(
        @NotBlank(message = "El nombre es obligatorio") String name,
        @Positive(message = "El precio debe ser mayor a 0") Double price,
        @Min(value = 0, message = "El stock no puede ser negativo") Integer stock
) {
}
