package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.BarberDTO;
import com.jorgepozes04.barbershop_api.dto.BarberResponseDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.Barbershop;
import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberServiceTest {
    @Mock
    private BarberRepository barberRepository;

    @InjectMocks
    private BarberService barberService;

    @Test
    @DisplayName("Should register a new barber successfully")
    void testRegisterBarber() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername("barber1");
        userCredentials.setPassword("password123");
        userCredentials.setRole(Role.BARBER);

        BarberDTO inputDTO = new BarberDTO("Theodoro", "12345678900", "barber", "pass");

        Barber savedBarber = new Barber(1L, userCredentials, "Theodoro", "12345678900", new Barbershop());
        when(barberRepository.save(any(Barber.class))).thenReturn(savedBarber);

        BarberResponseDTO result = barberService.register(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Theodoro", result.getName());

        verify(barberRepository, times(1)).save(any(Barber.class));
    }
}
