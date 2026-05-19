package com.example.stationservice.dto;

public record StationCommand(
        String name,
        String type,
        String status,
        String specs,
        boolean available
) {}