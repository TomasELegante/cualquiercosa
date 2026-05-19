package com.example.arenawallet.dto;

public record BilleteraResponse(
        Long id,
        Long idUsuario,
        Double saldo,
        Integer puntosFidelizacion
) {}