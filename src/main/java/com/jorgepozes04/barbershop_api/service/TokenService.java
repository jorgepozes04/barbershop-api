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
     * Generates a JWT token with issuer, subject, and role claim.
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
            log.info("JWT token generated: {}", user.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Token generation failed", exception);
            throw new RuntimeException("Error generating token", exception);
        }
    }

    /**
     * Validates JWT token signature and expiration. Returns the username claim.
     *
     * @param token JWT token to validate
     * @return authenticated username
     * @throws UnauthorizedException if token is invalid or expired
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
            log.debug("Token validated: {}", username);
            return username;
        } catch (JWTVerificationException exception) {
            log.warn("Token validation failed", exception);
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
    }
}