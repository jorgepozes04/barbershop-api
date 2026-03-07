package com.jorgepozes04.barbershop_api.dto;

import com.jorgepozes04.barbershop_api.enums.Day;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class WorkScheduleDTO {
    private Long barberId;
    private Day dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}
