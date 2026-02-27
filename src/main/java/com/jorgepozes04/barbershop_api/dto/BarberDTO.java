package com.jorgepozes04.barbershop_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarberDTO {
    private String name;
    private String cpf;
    private String username;
    private String password;
}
