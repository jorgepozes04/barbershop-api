package com.jorgepozes04.barbershop_api.controller;

import com.jorgepozes04.barbershop_api.dto.ClientSignupDTO;
import com.jorgepozes04.barbershop_api.dto.LoginDTO;
import com.jorgepozes04.barbershop_api.dto.TokenResponseDTO;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.service.AuthService;
import com.jorgepozes04.barbershop_api.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AuthService authService;

    /**
     * Authenticates user credentials and returns a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        log.info("Authentication attempt: {}", data.getUsername());
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserCredentials) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    /**
     * Registers a new client with login credentials.
     */
    @PostMapping("/client-signup")
    public ResponseEntity<Void> registerClient(@RequestBody @Valid ClientSignupDTO data) {
        log.info("Client registration initiated: {}", data.getCpf());
        authService.registerClient(data.getCpf(), data.getPassword(), data.getName(), data.getPhoneNumber());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}