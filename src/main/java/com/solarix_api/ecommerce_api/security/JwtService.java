package com.solarix_api.ecommerce_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.solarix_api.ecommerce_api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService{

    @Value("${jwt.secret}")
    // Clave secreta para generar el token
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    // Cuando expira el token generado
    private Long expiracion;

    @Value("${jwt.issuer}")
    // Quien firma el token
    private String issuer;

    /**
     * Genera un token JWT para el usuario autenticado
     * @param user Usuario autenticado
     * @return Token JWT firmado
     */
    public String generarToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiracion))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar el token: " + e.getMessage());
        }
    }

    /**
     * Extrae el email (subject) del token
     * @param token Token JWT
     * @return Email del usuario
     */

    public String getEmailFromSubject(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }

    /**
     * Valida si el token es valido y no ha expirado
     * @param token Token JWT
     * @return true si es válido
     */
    public boolean validarToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
