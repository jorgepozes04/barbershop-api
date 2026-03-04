package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.WorkScheduleDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import com.jorgepozes04.barbershop_api.repository.WorkScheduleRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final BarberRepository barberRepository;

    public WorkScheduleDTO createSchedule(Long barberId, @Valid WorkScheduleDTO dto) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found!"));

        Optional<WorkSchedule> existingSchedule = workScheduleRepository.findByBarberIdAndDayOfWeek(barberId,
                dto.getDayOfWeek());

        if (existingSchedule.isPresent()) {
            throw new IllegalArgumentException("WorkSchedule already exists!");
        }

        WorkSchedule schedule = new WorkSchedule();
        schedule.setBarber(barber);
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setBreakStartTime(dto.getBreakStartTime());
        schedule.setBreakEndTime(dto.getBreakEndTime());

        workScheduleRepository.save(schedule);

        return dto;
    }
}
