package com.example.stationservice.dto;

public record StationResult(
        Long id,
        String name,
        String type,
        String status,
        String specs,
        boolean available
) {}