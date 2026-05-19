package com.example.arenareservas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ReservaRequest(

        @NotNull(message = "El ID de usuario es obligatorio")
        @Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
        Long usuarioId,

        @NotNull(message = "El ID de estación es obligatorio")
        @Min(value = 1, message = "El ID de estación debe ser mayor a 0")
        Long estacionId,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotBlank(message = "El bloque horario no puede estar vacío")
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$",
                message = "Formato HH:mm-HH:mm (ej: 14:00-15:00)"
        )
        String bloqueHorario
) {}