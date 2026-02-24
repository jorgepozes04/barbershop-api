package com.jorgepozes04.barbershop_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "barbers")
public class Barber extends User {

}
