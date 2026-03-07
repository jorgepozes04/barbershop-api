package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.AppointmentResponseDTO;
import com.jorgepozes04.barbershop_api.dto.AppointmentGuestDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.AppointmentMapper;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.entities.Appointment;
import com.jorgepozes04.barbershop_api.entities.Client;
import com.jorgepozes04.barbershop_api.entities.ServiceOffered;
import com.jorgepozes04.barbershop_api.enums.Day;
import com.jorgepozes04.barbershop_api.enums.Status;
import com.jorgepozes04.barbershop_api.exception.BadRequestException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final ServiceOfferedRepository serviceOfferedRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;
    private final AppointmentMapper appointmentMapper;

    /**
     * Computes available appointment slots for a barber on a specific date,
     * considering work schedule, break time, and existing appointments.
     *
     * @param barberID  the barber's ID
     * @param serviceID the service ID to determine duration
     * @param date      the appointment date
     * @return list of available start times
     * @throws ResourceNotFoundException if barber or service not found
     * @throws BadRequestException       if barber not available on that day
     */
    public List<LocalTime> getAvailableTimeSlots(Long barberID, Long serviceID, LocalDate date) {
        Day weekDay = Day.valueOf(date.getDayOfWeek().name());

        WorkSchedule schedule = workScheduleRepository.findByBarberIdAndDayOfWeek(barberID, weekDay)
                .orElseThrow(() -> new BadRequestException("Barber is not working on this day"));

        ServiceOffered serviceOffered = serviceOfferedRepository.findById(serviceID)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceID));

        LocalDateTime startOfDay = date.atTime(schedule.getStartTime());
        LocalDateTime endOfDay = date.atTime(schedule.getEndTime());
        List<Appointment> existingAppointments = appointmentRepository.findByBarberIdAndStartTimeBetween(barberID,
                startOfDay, endOfDay);

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime currentTime = schedule.getStartTime();
        int serviceDuration = serviceOffered.getDuration();

        while (currentTime.plusMinutes(serviceDuration).isBefore(schedule.getEndTime()) ||
                currentTime.plusMinutes(serviceDuration).equals(schedule.getEndTime())) {
            LocalTime potentialEndTime = currentTime.plusMinutes(serviceDuration);
            boolean isAvailable = true;

            // Conflict with break time
            if (currentTime.isBefore(schedule.getBreakEndTime())
                    && potentialEndTime.isAfter(schedule.getBreakStartTime())) {
                isAvailable = false;
            }

            // Conflict with existing appointments
            if (isAvailable) {
                for (Appointment appointment : existingAppointments) {
                    LocalTime appointmentStart = appointment.getStartTime().toLocalTime();
                    LocalTime appointmentEnd = appointment.getEndTime().toLocalTime();
                    if (currentTime.isBefore(appointmentEnd) && potentialEndTime.isAfter(appointmentStart)) {
                        isAvailable = false;
                        break;
                    }
                }
            }

            if (isAvailable) {
                availableSlots.add(currentTime);
            }
            currentTime = currentTime.plusMinutes(15);
        }
        return availableSlots;
    }

    /**
     * Creates and persists a new appointment with availability validation.
     *
     * @param dto appointment booking details
     * @return the created appointment
     * @throws ValidationException       if input is invalid
     * @throws BadRequestException       if time slot is unavailable
     * @throws ResourceNotFoundException if resources not found
     */
    @Transactional
    public AppointmentResponseDTO bookAppointment(AppointmentGuestDTO dto) {
        log.info("Booking appointment for client CPF: {}", dto.getClientCpf());
        validateAppointmentDTO(dto);

        ServiceOffered service = serviceOfferedRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + dto.getServiceId()));

        LocalDateTime endTime = dto.getDateTime().plusMinutes(service.getDuration());
        boolean conflict = appointmentRepository.existsByBarberIdAndStartTimeLessThanAndEndTimeGreaterThan(
                dto.getBarberId(), endTime, dto.getDateTime());
        if (conflict) {
            log.warn("Time slot conflict for barber {} at {}", dto.getBarberId(), dto.getDateTime());
            throw new BadRequestException("Time slot is not available");
        }

        Client client = clientRepository.findByCpf(dto.getClientCpf())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setPhoneNumber(dto.getClientPhoneNumber());
                    newClient.setCpf(dto.getClientCpf());
                    newClient.setName(dto.getClientName());
                    return clientRepository.save(newClient);
                });

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setServiceOffered(service);
        appointment.setBarber(barberRepository.findById(dto.getBarberId())
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with ID: " + dto.getBarberId())));
        appointment.setStartTime(dto.getDateTime());
        appointment.setEndTime(endTime);
        appointment.setStatus(Status.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment booked successfully with ID: {}", savedAppointment.getId());
        return appointmentMapper.toResponseDTO(savedAppointment);
    }

    public List<AppointmentResponseDTO> getAllAppointmentsByBarber(Long barberId) {
        return appointmentRepository.findByBarberId(barberId)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .toList();
    }

    public List<AppointmentResponseDTO> getAppointmentsByClientId(Long clientId) {
        return appointmentRepository.findByClientId(clientId)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .toList();
    }

    private void validateAppointmentDTO(AppointmentGuestDTO dto) {
        if (dto == null) {
            throw new ValidationException("Appointment data cannot be null");
        }
        if (dto.getClientCpf() == null || dto.getClientCpf().isBlank()) {
            throw new ValidationException("Client CPF is required");
        }
        if (dto.getClientName() == null || dto.getClientName().isBlank()) {
            throw new ValidationException("Client name is required");
        }
        if (dto.getClientPhoneNumber() == null || dto.getClientPhoneNumber().isBlank()) {
            throw new ValidationException("Client phone number is required");
        }
        if (dto.getBarberId() == null) {
            throw new ValidationException("Barber ID is required");
        }
        if (dto.getServiceId() == null) {
            throw new ValidationException("Service ID is required");
        }
        if (dto.getDateTime() == null) {
            throw new ValidationException("Appointment date/time is required");
        }
    }
}
