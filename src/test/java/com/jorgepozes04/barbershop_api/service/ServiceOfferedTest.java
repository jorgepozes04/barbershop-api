package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.ServiceOfferedDTO;
import com.jorgepozes04.barbershop_api.entities.Barbershop;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import com.jorgepozes04.barbershop_api.repository.ServiceOfferedRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceOfferedTest {
    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;

    @InjectMocks
    private ServiceOfferedService serviceOfferedService;

    @Test
    @DisplayName("Should register a new service successfully")
    void testRegisterService() {
        ServiceOfferedDTO inputDTO = new ServiceOfferedDTO("Haircut", BigDecimal.valueOf(20), 30, "A stylish haircut");

        ServiceOffered savedService = new ServiceOffered(1L, "Haircut", BigDecimal.valueOf(20), 30,
                "A stylish haircut", new Barbershop());
        when(serviceOfferedRepository.save(any(ServiceOffered.class))).thenReturn(savedService);

        ServiceOfferedDTO result = serviceOfferedService.register(inputDTO);

        assertNotNull(result);
        assertEquals("Haircut", result.getName());
        assertEquals(BigDecimal.valueOf(20), result.getPrice());
        assertEquals(30, result.getDuration());
        assertEquals("A stylish haircut", result.getDescription());

        verify(serviceOfferedRepository, times(1)).save(any(ServiceOffered.class));
    }
}
