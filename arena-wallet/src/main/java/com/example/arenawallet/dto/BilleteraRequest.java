package com.example.arenawallet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BilleteraRequest(

        @NotNull(message = "El ID de usuario es obligatorio")
        @Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
        Long idUsuario,

        @NotNull(message = "El saldo es obligatorio")
        @PositiveOrZero(message = "El saldo no puede ser negativo")
        Double saldo,

        @NotNull(message = "Los puntos son obligatorios")
        @PositiveOrZero(message = "Los puntos no pueden ser negativos")
        Integer puntosFidelizacion
) {}