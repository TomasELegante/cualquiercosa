package com.example.arenareservas.dto;

import java.time.LocalDate;

public record ReservaCommand(
        Long usuarioId,
        Long estacionId,
        LocalDate fecha,
        String bloqueHorario
) {}