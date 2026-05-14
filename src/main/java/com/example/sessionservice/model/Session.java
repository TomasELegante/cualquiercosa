package com.example.sessionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de usuario no puede ser nulo")
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "El ID de estación no puede ser nulo")
    @Column(nullable = false)
    private Long stationId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(nullable = false)
    private Integer durationMinutes;

    public enum SessionStatus {
        ACTIVE, FINISHED, CANCELLED
    }
}