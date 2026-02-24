package com.jorgepozes04.barbershop_api.entities;

import com.jorgepozes04.barbershop_api.enums.Day;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="work_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Barber barber;
}
