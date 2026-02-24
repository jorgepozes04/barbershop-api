package com.jorgepozes04.barbershop_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "barbers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Barber extends User {
}
