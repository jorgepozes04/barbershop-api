package com.jorgepozes04.barbershop_api.enums;

public enum Role {
    BARBER("barber"),
    CUSTOMER("customer");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
