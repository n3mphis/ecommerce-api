package com.solarix_api.ecommerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrarRequest(
        @NotBlank(message = "El mail no puede estar vacío")
        @Email(message = "El mail debe ser válido")
        String email,
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password
) {
}
