package com.jorgepozes04.barbershop_api.dto;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentGuestDTO {
    private Long serviceId;
    private Long barberId;
    @Future
    private LocalDateTime dateTime;
    @CPF
    private String clientCpf;
    private String clientName;
    private String clientPhoneNumber;
}
