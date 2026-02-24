package com.jorgepozes04.barbershop_api.entities;

import com.jorgepozes04.barbershop_api.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client extends User {
    private String cpf;
    private String phoneNumber;

    public Client(){}

    public Client(String name, String email, String password, Role role, String cpf, String phoneNumber) {
        super(name, email, password, role);
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
