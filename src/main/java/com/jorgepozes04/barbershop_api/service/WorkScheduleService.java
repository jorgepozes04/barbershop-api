package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.WorkScheduleDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import com.jorgepozes04.barbershop_api.repository.WorkScheduleRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final BarberRepository barberRepository;

    /**
     * Creates a work schedule for a barber on a specific day.
     *
     * @param barberId the barber's ID
     * @param dto      the schedule details
     * @return the created schedule
     * @throws ValidationException       if validation fails
     * @throws ConflictException         if schedule already exists for that day
     * @throws ResourceNotFoundException if barber not found
     */
    @Transactional
    public WorkScheduleDTO createSchedule(Long barberId, @Valid WorkScheduleDTO dto) {
        log.info("Creating work schedule for barber ID: {} on {}", barberId, dto.getDayOfWeek());
        validateWorkScheduleDTO(dto);

        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + barberId));

        if (workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, dto.getDayOfWeek()).isPresent()) {
            log.warn("Work schedule already exists for barber {} on {}", barberId, dto.getDayOfWeek());
            throw new ConflictException("WorkSchedule already exists for this day!");
        }

        WorkSchedule schedule = new WorkSchedule();
        schedule.setBarber(barber);
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setBreakStartTime(dto.getBreakStartTime());
        schedule.setBreakEndTime(dto.getBreakEndTime());

        workScheduleRepository.save(schedule);
        log.info("Work schedule created successfully for barber ID: {}", barberId);
        return dto;
    }

    private void validateWorkScheduleDTO(WorkScheduleDTO dto) {
        if (dto == null) {
            throw new ValidationException("Work schedule data cannot be null");
        }
        if (dto.getDayOfWeek() == null) {
            throw new ValidationException("Day of week is required");
        }
        if (dto.getStartTime() == null) {
            throw new ValidationException("Start time is required");
        }
        if (dto.getEndTime() == null) {
            throw new ValidationException("End time is required");
        }
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new ValidationException("Start time must be before end time");
        }
        if (dto.getBreakStartTime() != null && dto.getBreakEndTime() != null) {
            if (dto.getBreakStartTime().isAfter(dto.getBreakEndTime())) {
                throw new ValidationException("Break start time must be before break end time");
            }
        }
    }
}
