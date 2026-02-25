package com.jorgepozes04.barbershop_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    @NotBlank
    private String name;
    @CPF
    @NotBlank
    private String cpf;
    private String phoneNumber;
}
