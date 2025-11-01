package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.ProductResponse;
import com.solarix_api.ecommerce_api.dto.ProductUpdateRequest;
import com.solarix_api.ecommerce_api.dto.ProductoCreateRequest;
import com.solarix_api.ecommerce_api.dto.UsersResponse;
import com.solarix_api.ecommerce_api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> todosLosUsuarios() {
        return ResponseEntity.ok(adminService.listarTodosLosUsuarios());
    }

    @GetMapping("/products/list")
    public ResponseEntity<List<ProductResponse>> todosLosProductos() {
        return ResponseEntity.ok(adminService.listarTodosLosProductos());
    }

    @PutMapping("/products/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> actualizarProducto(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        ProductResponse productResponse = adminService.actualizarProducto(id, request);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/products/delete/{id}")
    @PreAuthorize("hasROle('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        adminService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> crearProducto(@RequestBody @Valid ProductoCreateRequest request) {
        ProductResponse creado = adminService.crearNuevoProducto(request);
        return ResponseEntity.status(201).body(creado);
    }
}
