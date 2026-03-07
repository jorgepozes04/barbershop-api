package com.jorgepozes04.barbershop_api.config;

import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.repository.UserCredentialsRepository;
import com.jorgepozes04.barbershop_api.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserCredentials testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserCredentials();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.CLIENT);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void testLoadUserByUsernameSuccess() {
        // Arrange
        when(userCredentialsRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void testLoadUserByUsernameNotFound() {
        // Arrange
        when(userCredentialsRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    @DisplayName("Should load barber user successfully")
    void testLoadBarberUserSuccess() {
        // Arrange
        UserCredentials barber = new UserCredentials();
        barber.setId(2L);
        barber.setUsername("barber1");
        barber.setPassword("barberPassword");
        barber.setRole(Role.BARBER);

        when(userCredentialsRepository.findByUsername("barber1")).thenReturn(Optional.of(barber));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("barber1");

        // Assert
        assertNotNull(userDetails);
        assertEquals("barber1", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_BARBER")));
    }
}
