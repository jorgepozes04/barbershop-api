package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.Appointment;
import com.jorgepozes04.barbershop_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarberIdAndStartTimeBetween(Long barberId, LocalDateTime start, LocalDateTime end);

    boolean existsByBarberIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long barberId, LocalDateTime endTime, LocalDateTime startTime);
}
