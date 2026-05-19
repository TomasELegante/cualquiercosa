package com.example.sessionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de usuario no puede ser nulo")
    @Column(nullable = false)
    private Long userId;

    @Column(length = 100)
    private String username;

    @NotNull(message = "El ID de estación no puede ser nulo")
    @Column(nullable = false)
    private Long stationId;

    @Column(length = 100)
    private String stationName;

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