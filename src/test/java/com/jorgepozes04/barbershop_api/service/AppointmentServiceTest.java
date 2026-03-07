package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.AppointmentResponseDTO;
import com.jorgepozes04.barbershop_api.dto.mapper.AppointmentMapper;
import com.jorgepozes04.barbershop_api.entities.*;
import com.jorgepozes04.barbershop_api.enums.Day;
import com.jorgepozes04.barbershop_api.enums.Status;
import com.jorgepozes04.barbershop_api.exception.BadRequestException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentService Tests")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private WorkScheduleRepository workScheduleRepository;

    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private Barber testBarber;
    private Client testClient;
    private ServiceOffered testService;
    private WorkSchedule testWorkSchedule;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        // Setup Barber
        testBarber = new Barber();
        testBarber.setId(1L);
        testBarber.setName("Jane Smith");
        testBarber.setCpf("98765432101");

        // Setup Client
        testClient = new Client();
        testClient.setId(1L);
        testClient.setName("John Doe");
        testClient.setCpf("12345678901");

        // Setup Service
        testService = new ServiceOffered();
        testService.setId(1L);
        testService.setName("Haircut");
        testService.setDuration(30);
        testService.setPrice(50.0);

        // Setup Work Schedule
        testWorkSchedule = new WorkSchedule();
        testWorkSchedule.setId(1L);
        testWorkSchedule.setBarber(testBarber);
        testWorkSchedule.setDayOfWeek(Day.MONDAY);
        testWorkSchedule.setStartTime(LocalTime.of(9, 0));
        testWorkSchedule.setEndTime(LocalTime.of(18, 0));
        testWorkSchedule.setBreakStartTime(LocalTime.of(12, 0));
        testWorkSchedule.setBreakEndTime(LocalTime.of(13, 0));

        // Setup Appointment
        testAppointment = new Appointment();
        testAppointment.setId(1L);
        testAppointment.setBarber(testBarber);
        testAppointment.setClient(testClient);
        testAppointment.setService(testService);
        testAppointment.setStartTime(LocalDateTime.of(2026, 3, 9, 10, 0));
        testAppointment.setEndTime(LocalDateTime.of(2026, 3, 9, 10, 30));
        testAppointment.setStatus(Status.SCHEDULED);
    }

    @Test
    @DisplayName("Should get available time slots successfully")
    void testGetAvailableTimeSlotsSuccess() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9); // Monday
        Long barberId = 1L;
        Long serviceId = 1L;

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));
        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(testService));
        when(appointmentRepository.findByBarberIdAndStartTimeBetween(
                eq(barberId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // Act
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate);

        // Assert
        assertNotNull(availableSlots);
        assertFalse(availableSlots.isEmpty());
        verify(workScheduleRepository, times(1)).findByBarberIdAndDayOfWeek(barberId, Day.MONDAY);
        verify(serviceOfferedRepository, times(1)).findById(serviceId);
    }

    @Test
    @DisplayName("Should throw BadRequestException when barber not working on date")
    void testGetAvailableTimeSlotsBarbernNotWorking() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9);
        Long barberId = 1L;
        Long serviceId = 1L;

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when service not found")
    void testGetAvailableTimeSlotsServiceNotFound() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9);
        Long barberId = 1L;
        Long serviceId = 999L;

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));
        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate));
    }

    @Test
    @DisplayName("Should exclude break time from available slots")
    void testGetAvailableTimeSlotsExcludesBreakTime() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9);
        Long barberId = 1L;
        Long serviceId = 1L;

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));
        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(testService));
        when(appointmentRepository.findByBarberIdAndStartTimeBetween(
                eq(barberId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // Act
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate);

        // Assert - Break time (12:00-13:00) should not have appointments
        for (LocalTime slot : availableSlots) {
            assertFalse(slot.isAfter(LocalTime.of(11, 30)) && slot.isBefore(LocalTime.of(13, 0)),
                    "Slot " + slot + " should not be during break time");
        }
    }

    @Test
    @DisplayName("Should exclude existing appointments from available slots")
    void testGetAvailableTimeSlotsExcludesExistingAppointments() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9);
        Long barberId = 1L;
        Long serviceId = 1L;

        Appointment existingAppointment = new Appointment();
        existingAppointment.setStartTime(LocalDateTime.of(2026, 3, 9, 10, 0));
        existingAppointment.setEndTime(LocalDateTime.of(2026, 3, 9, 10, 30));

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));
        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(testService));
        when(appointmentRepository.findByBarberIdAndStartTimeBetween(
                eq(barberId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(List.of(existingAppointment));

        // Act
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate);

        // Assert
        assertNotNull(availableSlots);
        for (LocalTime slot : availableSlots) {
            assertFalse(slot.equals(LocalTime.of(10, 0)),
                    "Slot 10:00 should not be available due to existing appointment");
        }
    }

    @Test
    @DisplayName("Should return empty list when no slots available")
    void testGetAvailableTimeSlotsReturnEmptyList() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2026, 3, 9);
        Long barberId = 1L;
        Long serviceId = 1L;

        // Add appointments that fill entire day
        List<Appointment> appointments = new ArrayList<>();
        for (int hour = 9; hour < 18; hour++) {
            Appointment apt = new Appointment();
            apt.setStartTime(LocalDateTime.of(2026, 3, 9, hour, 0));
            apt.setEndTime(LocalDateTime.of(2026, 3, 9, hour, 30));
            appointments.add(apt);
        }

        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));
        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(testService));
        when(appointmentRepository.findByBarberIdAndStartTimeBetween(
                eq(barberId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(appointments);

        // Act
        List<LocalTime> availableSlots = appointmentService.getAvailableTimeSlots(barberId, serviceId, appointmentDate);

        // Assert
        assertNotNull(availableSlots);
        assertTrue(availableSlots.isEmpty());
    }
}
