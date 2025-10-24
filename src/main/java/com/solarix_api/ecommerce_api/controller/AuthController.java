package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.LoginRequest;
import com.solarix_api.ecommerce_api.dto.LoginResponse;
import com.solarix_api.ecommerce_api.dto.RegisterResponse;
import com.solarix_api.ecommerce_api.dto.RegistrarRequest;
import com.solarix_api.ecommerce_api.exception.EmailYaRegistradoException;
import com.solarix_api.ecommerce_api.model.Role;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import com.solarix_api.ecommerce_api.security.JwtService;
import com.solarix_api.ecommerce_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/registrar")
    public ResponseEntity<RegisterResponse> registrarUsuario(@RequestBody @Valid RegistrarRequest registrarRequest, Role role) {
        User user = userService.registrarUsuario(registrarRequest, Role.USER);
        RegisterResponse response = new RegisterResponse(user.getEmail());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> iniciarSesion(@RequestBody @Valid LoginRequest login) {
        User user = userService.autenticarUsuario(login);
        String token = jwtService.generarToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    // Exception Handler
    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<String> handleEmailYaRegistrado(EmailYaRegistradoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
