package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.ClientRepository;
import com.jorgepozes04.barbershop_api.repository.UserCredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AuthService authService;

    private Client testClient;
    private UserCredentials testCredentials;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setCpf("12345678901");
        testClient.setName("John Doe");
        testClient.setPhoneNumber("11999999999");

        testCredentials = new UserCredentials();
        testCredentials.setId(1L);
        testCredentials.setUsername("12345678901");
        testCredentials.setPassword("encodedPassword");
        testCredentials.setRole(Role.CLIENT);
    }

    @Test
    @DisplayName("Should register client successfully with valid data")
    void testRegisterClientSuccess() {
        // Arrange
        String cpf = "12345678901";
        String password = "password123";
        String name = "John Doe";
        String phone = "11999999999";

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(new Client()));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act & Assert
        assertDoesNotThrow(() -> authService.registerClient(cpf, password, name, phone));
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when client CPF already has credentials")
    void testRegisterClientWithExistingCredentials() {
        // Arrange
        Client existingClient = new Client();
        existingClient.setUserCredentials(testCredentials);

        when(clientRepository.findByCpf("12345678901")).thenReturn(Optional.of(existingClient));

        // Act & Assert
        assertThrows(ConflictException.class,
                () -> authService.registerClient("12345678901", "password123", "John Doe", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when CPF is null")
    void testRegisterClientWithNullCpf() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient(null, "password123", "John Doe", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when CPF is blank")
    void testRegisterClientWithBlankCpf() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("", "password123", "John Doe", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when password is less than 8 characters")
    void testRegisterClientWithShortPassword() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", "short", "John Doe", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when password is null")
    void testRegisterClientWithNullPassword() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", null, "John Doe", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when name is null")
    void testRegisterClientWithNullName() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", "password123", null, "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when name is blank")
    void testRegisterClientWithBlankName() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", "password123", "", "11999999999"));
    }

    @Test
    @DisplayName("Should throw ValidationException when phone is null")
    void testRegisterClientWithNullPhone() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", "password123", "John Doe", null));
    }

    @Test
    @DisplayName("Should throw ValidationException when phone is blank")
    void testRegisterClientWithBlankPhone() {
        // Act & Assert
        assertThrows(ValidationException.class,
                () -> authService.registerClient("12345678901", "password123", "John Doe", ""));
    }

    @Test
    @DisplayName("Should create new client if CPF doesn't exist")
    void testRegisterClientCreatesNewClient() {
        // Arrange
        String cpf = "12345678901";
        String password = "password123";
        String name = "John Doe";
        String phone = "11999999999";

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act
        assertDoesNotThrow(() -> authService.registerClient(cpf, password, name, phone));

        // Assert
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(passwordEncoder, times(1)).encode(password);
    }
}
