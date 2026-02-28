package com.jorgepozes04.barbershop_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private String clientName;
    private String clientPhoneNumber;
    private String serviceName;
    private String barberName;
    private String barbershopName;
    private LocalDateTime dateTime;
}
