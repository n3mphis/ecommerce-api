package com.solarix_api.ecommerce_api.service;

import com.solarix_api.ecommerce_api.dto.LoginRequest;
import com.solarix_api.ecommerce_api.dto.RegistrarRequest;
import com.solarix_api.ecommerce_api.exception.AutenticacionFallidaException;
import com.solarix_api.ecommerce_api.exception.EmailNoEncontradoException;
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

    /**
     * Lo iba a poner en application.properties pero lo dejo aca para que se pueda probar
     */
    private static final String[] ADMIN_EMAILS = {
            "admin1@solix.com",
            "admin2@solix.com",
            "admin3@solix.com",
            "admin4@solix.com"
    };

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrarUsuario(RegistrarRequest registrarRequest, Role roleDefault) {
        if (userRepository.findByEmail(registrarRequest.email()).isPresent()) {
            throw new EmailYaRegistradoException("El email: " + registrarRequest.email() + " ya está registrado");
        }

        Role role = roleDefault;
        for (String adminEmail : ADMIN_EMAILS) {
            if (registrarRequest.email().equals(adminEmail)) {
                role = Role.ADMIN;
                break;
            }
        }

        User usuario = new User(
                registrarRequest.email(),
                passwordEncoder.encode(registrarRequest.password()),
                role);

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
