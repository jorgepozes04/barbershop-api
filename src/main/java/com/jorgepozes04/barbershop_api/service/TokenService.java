package com.jorgepozes04.barbershop_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Generate JWT token for authenticated user
     */
    public String generateToken(UserCredentials user) {
        try {
            log.debug("Generating JWT token for user: {}", user.getUsername());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("barbershop-api")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            log.info("JWT token generated successfully for user: {}", user.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Error generating JWT token", exception);
            throw new RuntimeException("Error generating token", exception);
        }
    }

    /**
     * Validate JWT token and extract username
     * 
     * @param token the JWT token to validate
     * @return the username from the token
     * @throws UnauthorizedException if token is invalid
     */
    public String validateToken(String token) {
        try {
            log.debug("Validating JWT token");
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String username = JWT.require(algorithm)
                    .withIssuer("barbershop-api")
                    .build()
                    .verify(token)
                    .getSubject();
            log.debug("JWT token validated successfully for user: {}", username);
            return username;
        } catch (JWTVerificationException exception) {
            log.warn("JWT token validation failed: {}", exception.getMessage());
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    /**
     * Generate token expiration date (2 hours from now)
     */
    private Instant genExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
    }
}