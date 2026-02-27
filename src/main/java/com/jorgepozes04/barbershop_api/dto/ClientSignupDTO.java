package com.jorgepozes04.barbershop_api.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class ClientSignupDTO {
    @CPF
    private String cpf;
    private String name;
    private String password;
    private String phoneNumber;
}
