package com.example.arenareservas.dto;

import java.time.LocalDate;

public record ReservaResponse(
        Long id,
        Long usuarioId,
        Long estacionId,
        LocalDate fecha,
        String bloqueHorario,
        String estado
) {}