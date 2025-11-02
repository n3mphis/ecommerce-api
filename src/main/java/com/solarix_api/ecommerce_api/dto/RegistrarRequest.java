package com.solarix_api.ecommerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrarRequest(
        @NotBlank(message = "El mail no puede estar vacío")
        @Email(
                message = "El mail debe ser válido",
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password
) {
}
