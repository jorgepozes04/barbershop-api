package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {
}
