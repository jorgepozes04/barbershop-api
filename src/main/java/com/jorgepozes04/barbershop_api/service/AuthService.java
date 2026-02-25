package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import com.jorgepozes04.barbershop_api.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsRepository userCredentialsRepository;
    private final ClientRepository clientRepository;

    public void registerClient(String cpf, String password, String name, String phone) {
        Client client = clientRepository.findByCpf(cpf)
                .orElse(new Client());

        if (client.getUserCredentials() != null) {
            throw new RuntimeException("This CPF is already registered with credentials.");
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
    }
}
