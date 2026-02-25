package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.AppointmentGuestDTO;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.enums.Day;
import com.jorgepozes04.barbershop_api.enums.Role;
import com.jorgepozes04.barbershop_api.enums.Status;
import com.jorgepozes04.barbershop_api.repository.*;
import com.jorgepozes04.barbershop_api.entities.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public List<LocalTime> getAvailableTimeSlots(Long barberID, Long serviceID, LocalDate date) {
        Day weekDay = Day.valueOf(date.getDayOfWeek().name());

        WorkSchedule schedule = workScheduleRepository.findByBarberIdAndDayOfWeek(barberID, weekDay)
                .orElseThrow(() -> new RuntimeException("Barber is not working on this day"));

        Service service = serviceRepository.findById(serviceID)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        LocalDateTime startOfDay = date.atTime(schedule.getStartTime());
        LocalDateTime endOfDay = date.atTime(schedule.getEndTime());
        List<Appointment> existingAppointments = appointmentRepository.findByBarberIdAndStartTimeBetween(barberID, startOfDay, endOfDay);

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime currentTime = schedule.getStartTime();
        int serviceDuration = service.getDuration();

        while (currentTime.plusMinutes(serviceDuration).isBefore(schedule.getEndTime()) ||
                currentTime.plusMinutes(serviceDuration).equals(schedule.getEndTime()))
        {
            LocalTime potentialEndTime = currentTime.plusMinutes(serviceDuration);
            boolean isAvailable = true;

            if(currentTime.isBefore(schedule.getBreakEndTime()) && potentialEndTime.isAfter(schedule.getBreakStartTime())){
                isAvailable = false;
            }

            for(Appointment appointment : existingAppointments){
                LocalTime appointmentStart = appointment.getStartTime().toLocalTime();
                LocalTime appointmentEnd = appointment.getEndTime().toLocalTime();

                if(currentTime.isBefore(appointmentEnd) && potentialEndTime.isAfter(appointmentStart)){
                    isAvailable = false;
                    break;
                }
            }

            if(isAvailable){
                availableSlots.add(currentTime);
            }

            currentTime = currentTime.plusMinutes(30);
        }

        return availableSlots;
    }

    @Transactional
    public Appointment bookAppointment(AppointmentGuestDTO dto) {
        LocalDateTime endTime = dto.getDateTime().plusMinutes(
                serviceRepository.findById(dto.getServiceId()).get().getDuration()
        );

        boolean conflict = appointmentRepository.existsByBarberIdAndStartTimeLessThanAndEndTimeGreaterThan(
                dto.getBarberId(), endTime, dto.getDateTime()
        );
        if (conflict) {
            throw new RuntimeException("Time slot is not available");
        }

        Client client = clientRepository.findByCpf(dto.getClientCpf())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setPhoneNumber(dto.getClientPhoneNumber());
                    newClient.setCpf(dto.getClientCpf());
                    newClient.setName(dto.getClientName());
                    newClient.setRole(Role.CUSTOMER);
                    return clientRepository.save(newClient);
                });
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setService(serviceRepository.findById(dto.getServiceId()).orElseThrow());
        appointment.setBarber(barberRepository.findById(dto.getBarberId()).orElseThrow());
        appointment.setStartTime(dto.getDateTime());
        appointment.setEndTime(endTime);
        appointment.setStatus(Status.PENDING);

        return appointmentRepository.save(appointment);
    }
}
