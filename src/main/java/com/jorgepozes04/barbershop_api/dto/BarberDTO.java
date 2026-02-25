package com.jorgepozes04.barbershop_api.dto;

import com.jorgepozes04.barbershop_api.entities.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarberDTO {
    private Long id;
    private String name;
    private UserCredentials userCredentials;
}
