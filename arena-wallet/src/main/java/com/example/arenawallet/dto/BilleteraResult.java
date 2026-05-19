package com.example.arenawallet.dto;

public record BilleteraResult(
        Long id,
        Long idUsuario,
        Double saldo,
        Integer puntosFidelizacion
) {}