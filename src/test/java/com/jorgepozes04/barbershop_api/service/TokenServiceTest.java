package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenService Tests")
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private UserCredentials testUser;
    private static final String TEST_SECRET = "testsecret123456789012345678901234567890";

    @BeforeEach
    void setUp() {
        // Set the secret using reflection
        ReflectionTestUtils.setField(tokenService, "secret", TEST_SECRET);

        testUser = new UserCredentials();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.CLIENT);
    }

    @Test
    @DisplayName("Should generate valid token for authenticated user")
    void testGenerateTokenSuccess() {
        // Act
        String token = tokenService.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should validate token and extract username successfully")
    void testValidateTokenSuccess() {
        // Arrange
        String token = tokenService.generateToken(testUser);

        // Act
        String username = tokenService.validateToken(token);

        // Assert
        assertNotNull(username);
        assertEquals(testUser.getUsername(), username);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when validating invalid token")
    void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenService.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when validating null token")
    void testValidateNullToken() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenService.validateToken(null));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when validating tampered token")
    void testValidateTamperedToken() {
        // Arrange
        String token = tokenService.generateToken(testUser);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenService.validateToken(tamperedToken));
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void testGenerateDifferentTokensForDifferentUsers() {
        // Arrange
        UserCredentials user1 = new UserCredentials();
        user1.setUsername("user1");
        user1.setRole(Role.CLIENT);

        UserCredentials user2 = new UserCredentials();
        user2.setUsername("user2");
        user2.setRole(Role.BARBER);

        // Act
        String token1 = tokenService.generateToken(user1);
        String token2 = tokenService.generateToken(user2);

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should include username in token")
    void testTokenContainsUsername() {
        // Act
        String token = tokenService.generateToken(testUser);
        String extractedUsername = tokenService.validateToken(token);

        // Assert
        assertEquals(testUser.getUsername(), extractedUsername);
    }

    @Test
    @DisplayName("Should generate token with BARBER role")
    void testGenerateTokenWithBarberRole() {
        // Arrange
        UserCredentials barber = new UserCredentials();
        barber.setUsername("barber1");
        barber.setPassword("password");
        barber.setRole(Role.BARBER);

        // Act
        String token = tokenService.generateToken(barber);
        String username = tokenService.validateToken(token);

        // Assert
        assertNotNull(token);
        assertEquals("barber1", username);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException for empty token string")
    void testValidateEmptyToken() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenService.validateToken(""));
    }

    @Test
    @DisplayName("Should generate token with correct issuer")
    void testTokenContainsCorrectIssuer() {
        // Act
        String token = tokenService.generateToken(testUser);

        // Assert - Token should be valid and contain the correct issuer
        assertDoesNotThrow(() -> tokenService.validateToken(token));
    }
}
