package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.LoginRequest;
import com.solarix_api.ecommerce_api.dto.RegistrarRequest;
import com.solarix_api.ecommerce_api.exception.AutenticacionFallidaException;
import com.solarix_api.ecommerce_api.exception.EmailNoEncontradoException;
import com.solarix_api.ecommerce_api.exception.EmailYaRegistradoException;
import com.solarix_api.ecommerce_api.model.Role;
import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import com.solarix_api.ecommerce_api.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registrarUsuario(RegistrarRequest registrarRequest, Role role) {
        if (userRepository.findByEmail(registrarRequest.email()).isPresent()) {
            throw new EmailYaRegistradoException("El email: " + registrarRequest.email() + " ya está registrado");
        }
        User usuario = new User(registrarRequest.email(), passwordEncoder.encode(registrarRequest.password()), role);
        return userRepository.save(usuario);
    }

    public User autenticarUsuario(LoginRequest login) {
        User user = userRepository.findByEmail(login.email())
                .orElseThrow(() -> new EmailNoEncontradoException("No se encontro el email " + login.email()));

        if (!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new AutenticacionFallidaException("Credenciales inválidas");
        }

        return user;
    }
}
