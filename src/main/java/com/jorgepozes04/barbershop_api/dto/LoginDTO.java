package com.jorgepozes04.barbershop_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String username;
    private String password;
}
