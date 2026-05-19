package com.example.arenareservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El ID de estación es obligatorio")
    @Min(value = 1, message = "El ID de estación debe ser mayor a 0")
    @Column(nullable = false)
    private Long estacionId;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "El bloque horario no puede estar vacío")
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$",
            message = "El bloque horario debe tener formato HH:mm-HH:mm (ej: 14:00-15:00)"
    )
    @Column(nullable = false)
    private String bloqueHorario;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false)
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA
}