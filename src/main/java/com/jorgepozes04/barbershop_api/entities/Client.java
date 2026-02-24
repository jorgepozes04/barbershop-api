package com.jorgepozes04.barbershop_api.entities;

import com.jorgepozes04.barbershop_api.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User {
    private String cpf;
    private String phoneNumber;
}
