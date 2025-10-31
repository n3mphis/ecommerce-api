package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.dto.ProductResponse;
import com.solarix_api.ecommerce_api.dto.UsersResponse;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> todosLosUsuarios() {
        return ResponseEntity.ok(adminService.listarTodosLosUsuarios());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> todosLosProductos() {
        return ResponseEntity.ok(adminService.listarTodosLosProductos());
    }
}
