package com.jorgepozes04.barbershop_api.repository;

import com.jorgepozes04.barbershop_api.entities.Appointment;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.enums.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    Optional<WorkSchedule> findByBarberIdAndDayOfWeek(Long barberId, Day dayOfWeek);
}
