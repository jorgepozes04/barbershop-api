package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.Appointment;
import com.jorgepozes04.barbershop_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
