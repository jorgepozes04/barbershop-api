package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import com.jorgepozes04.barbershop_api.repository.UserCredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsRepository userCredentialsRepository;
    private final ClientRepository clientRepository;

    /**
     * Register a new client with authentication credentials
     * 
     * @param cpf      the client's CPF
     * @param password the authentication password
     * @param name     the client's full name
     * @param phone    the client's phone number
     * @throws ValidationException if input is invalid
     * @throws ConflictException   if CPF is already registered
     */
    @Transactional
    public void registerClient(String cpf, String password, String name, String phone) {
        log.info("Attempting to register client with CPF: {}", cpf);

        // Validate input
        validateRegistrationInput(cpf, password, name, phone);

        // Check if CPF already has credentials
        Client client = clientRepository.findByCpf(cpf)
                .orElse(new Client());

        if (client.getUserCredentials() != null) {
            log.warn("Attempt to register client with existing credentials for CPF: {}", cpf);
            throw new ConflictException("This CPF is already registered with credentials");
        }

        client.setCpf(cpf);
        client.setName(name);
        client.setPhoneNumber(phone);

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(cpf);
        credentials.setPassword(passwordEncoder.encode(password));
        credentials.setRole(Role.CLIENT);

        client.setUserCredentials(credentials);
        clientRepository.save(client);

        log.info("Client registered successfully with CPF: {}", cpf);
    }

    /**
     * Validate registration input
     */
    private void validateRegistrationInput(String cpf, String password, String name, String phone) {
        if (cpf == null || cpf.isBlank()) {
            throw new ValidationException("CPF is required");
        }
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }
        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Phone number is required");
        }
    }
}
