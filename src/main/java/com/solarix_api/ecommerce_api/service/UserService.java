package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.RegistrarRequest;
import com.solarix_api.ecommerce_api.exception.EmailYaRegistradoException;
import com.solarix_api.ecommerce_api.model.Role;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrarUsuario(RegistrarRequest registrarRequest, Role role) {
        if (userRepository.findByEmail(registrarRequest.email()).isPresent()) {
            throw new EmailYaRegistradoException("El email: " + registrarRequest.email() + " ya está registrado");
        }
        User usuario = new User(registrarRequest.email(), passwordEncoder.encode(registrarRequest.password()), role);
        return userRepository.save(usuario);
    }
}
