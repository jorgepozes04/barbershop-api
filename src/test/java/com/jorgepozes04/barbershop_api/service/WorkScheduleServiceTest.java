package com.jorgepozes04.barbershop_api.service;

import com.jorgepozes04.barbershop_api.dto.WorkScheduleDTO;
import com.jorgepozes04.barbershop_api.entities.Barber;
import com.jorgepozes04.barbershop_api.entities.WorkSchedule;
import com.jorgepozes04.barbershop_api.enums.Day;
import com.jorgepozes04.barbershop_api.exception.ConflictException;
import com.jorgepozes04.barbershop_api.exception.ResourceNotFoundException;
import com.jorgepozes04.barbershop_api.exception.ValidationException;
import com.jorgepozes04.barbershop_api.repository.BarberRepository;
import com.jorgepozes04.barbershop_api.repository.WorkScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkScheduleService Tests")
class WorkScheduleServiceTest {

    @Mock
    private WorkScheduleRepository workScheduleRepository;

    @Mock
    private BarberRepository barberRepository;

    @InjectMocks
    private WorkScheduleService workScheduleService;

    private Barber testBarber;
    private WorkSchedule testWorkSchedule;
    private WorkScheduleDTO testWorkScheduleDTO;

    @BeforeEach
    void setUp() {
        testBarber = new Barber();
        testBarber.setId(1L);
        testBarber.setName("Jane Smith");

        testWorkSchedule = new WorkSchedule();
        testWorkSchedule.setId(1L);
        testWorkSchedule.setBarber(testBarber);
        testWorkSchedule.setDayOfWeek(Day.MONDAY);
        testWorkSchedule.setStartTime(LocalTime.of(9, 0));
        testWorkSchedule.setEndTime(LocalTime.of(18, 0));
        testWorkSchedule.setBreakStartTime(LocalTime.of(12, 0));
        testWorkSchedule.setBreakEndTime(LocalTime.of(13, 0));

        testWorkScheduleDTO = new WorkScheduleDTO();
        testWorkScheduleDTO.setDayOfWeek(Day.MONDAY);
        testWorkScheduleDTO.setStartTime(LocalTime.of(9, 0));
        testWorkScheduleDTO.setEndTime(LocalTime.of(18, 0));
        testWorkScheduleDTO.setBreakStartTime(LocalTime.of(12, 0));
        testWorkScheduleDTO.setBreakEndTime(LocalTime.of(13, 0));
    }

    @Test
    @DisplayName("Should create work schedule successfully")
    void testCreateWorkScheduleSuccess() {
        // Arrange
        Long barberId = 1L;
        when(barberRepository.findById(barberId)).thenReturn(Optional.of(testBarber));
        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.empty());
        when(workScheduleRepository.save(any(WorkSchedule.class))).thenReturn(testWorkSchedule);

        // Act
        assertDoesNotThrow(() -> workScheduleService.createWorkSchedule(barberId, testWorkScheduleDTO));

        // Assert
        verify(workScheduleRepository, times(1)).save(any(WorkSchedule.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when barber not found")
    void testCreateWorkScheduleBarbernNotFound() {
        // Arrange
        Long barberId = 999L;
        when(barberRepository.findById(barberId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> workScheduleService.createWorkSchedule(barberId, testWorkScheduleDTO));
    }

    @Test
    @DisplayName("Should throw ConflictException when schedule already exists for day")
    void testCreateWorkScheduleAlreadyExists() {
        // Arrange
        Long barberId = 1L;
        when(barberRepository.findById(barberId)).thenReturn(Optional.of(testBarber));
        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));

        // Act & Assert
        assertThrows(ConflictException.class,
                () -> workScheduleService.createWorkSchedule(barberId, testWorkScheduleDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when start time is after end time")
    void testCreateWorkScheduleInvalidTimes() {
        // Arrange
        Long barberId = 1L;
        testWorkScheduleDTO.setStartTime(LocalTime.of(18, 0));
        testWorkScheduleDTO.setEndTime(LocalTime.of(9, 0));

        when(barberRepository.findById(barberId)).thenReturn(Optional.of(testBarber));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> workScheduleService.createWorkSchedule(barberId, testWorkScheduleDTO));
    }

    @Test
    @DisplayName("Should throw ValidationException when break times are invalid")
    void testCreateWorkScheduleInvalidBreakTimes() {
        // Arrange
        Long barberId = 1L;
        testWorkScheduleDTO.setBreakStartTime(LocalTime.of(13, 0));
        testWorkScheduleDTO.setBreakEndTime(LocalTime.of(12, 0));

        when(barberRepository.findById(barberId)).thenReturn(Optional.of(testBarber));

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> workScheduleService.createWorkSchedule(barberId, testWorkScheduleDTO));
    }

    @Test
    @DisplayName("Should get work schedule by barber and day successfully")
    void testGetWorkScheduleSuccess() {
        // Arrange
        Long barberId = 1L;
        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.of(testWorkSchedule));

        // Act
        WorkSchedule result = workScheduleService.getWorkSchedule(barberId, Day.MONDAY);

        // Assert
        assertNotNull(result);
        assertEquals(Day.MONDAY, result.getDayOfWeek());
        verify(workScheduleRepository, times(1)).findByBarberIdAndDayOfWeek(barberId, Day.MONDAY);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when schedule not found")
    void testGetWorkScheduleNotFound() {
        // Arrange
        Long barberId = 1L;
        when(workScheduleRepository.findByBarberIdAndDayOfWeek(barberId, Day.MONDAY))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> workScheduleService.getWorkSchedule(barberId, Day.MONDAY));
    }

    @Test
    @DisplayName("Should get all schedules for barber successfully")
    void testGetAllBarberSchedulesSuccess() {
        // Arrange
        Long barberId = 1L;
        List<WorkSchedule> schedules = Arrays.asList(testWorkSchedule);
        when(workScheduleRepository.findByBarberId(barberId)).thenReturn(schedules);

        // Act
        List<WorkSchedule> result = workScheduleService.getAllBarberSchedules(barberId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(workScheduleRepository, times(1)).findByBarberId(barberId);
    }

    @Test
    @DisplayName("Should return empty list when barber has no schedules")
    void testGetAllBarberSchedulesEmpty() {
        // Arrange
        Long barberId = 1L;
        when(workScheduleRepository.findByBarberId(barberId)).thenReturn(Arrays.asList());

        // Act
        List<WorkSchedule> result = workScheduleService.getAllBarberSchedules(barberId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should delete work schedule successfully")
    void testDeleteWorkScheduleSuccess() {
        // Arrange
        Long scheduleId = 1L;
        when(workScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(testWorkSchedule));

        // Act
        assertDoesNotThrow(() -> workScheduleService.deleteWorkSchedule(scheduleId));

        // Assert
        verify(workScheduleRepository, times(1)).deleteById(scheduleId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent schedule")
    void testDeleteWorkScheduleNotFound() {
        // Arrange
        Long scheduleId = 999L;
        when(workScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> workScheduleService.deleteWorkSchedule(scheduleId));
    }
}
