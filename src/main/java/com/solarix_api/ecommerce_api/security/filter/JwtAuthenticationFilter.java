package com.solarix_api.ecommerce_api.security.filter;

import com.solarix_api.ecommerce_api.model.User;
import com.solarix_api.ecommerce_api.repository.UserRepository;
import com.solarix_api.ecommerce_api.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Leer header Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no existe o no empieza con "Bearer ", continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer Token (quitar "Bearer ")
        String token = authHeader.substring(7);

        // Validar token
        if (!jwtService.validarToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer email del token
        String email = jwtService.getEmailFromSubject(token);

        // Buscar usuario en DB
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        User user = optionalUser.get();

        // Crear autenticacion
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,               // principal (quien es)
                null,               // credentials (no se usan en JWT)
                user.getAuthorities()       // roles: [ROLE_USER, ROLE_ADMIN]
        );

        // Agregar detalles (IP, sesion, etc)
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Guardar en SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Pasar al siguiente filtro
        filterChain.doFilter(request, response);
    }
}
