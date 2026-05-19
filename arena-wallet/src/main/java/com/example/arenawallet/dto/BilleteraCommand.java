package com.example.arenawallet.dto;

public record BilleteraCommand(
        Long idUsuario,
        Double saldo,
        Integer puntosFidelizacion
) {}