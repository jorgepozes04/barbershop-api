package com.jorgepozes04.barbershop_api.entities;

import com.jorgepozes04.barbershop_api.enums.Day;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="work_schedules")
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Day day;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;

    public WorkSchedule(){}

    public WorkSchedule(Day day, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime breakStartTime, LocalDateTime breakEndTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(LocalDateTime breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public LocalDateTime getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(LocalDateTime breakEndTime) {
        this.breakEndTime = breakEndTime;
    }
}
