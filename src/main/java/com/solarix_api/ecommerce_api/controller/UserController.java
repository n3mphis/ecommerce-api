package com.solarix_api.ecommerce_api.controller;

import com.solarix_api.ecommerce_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/me")
//    public ResponseEntity<Authentication> me(Authentication authentication) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return ResponseEntity.ok(auth);
//    }
}
