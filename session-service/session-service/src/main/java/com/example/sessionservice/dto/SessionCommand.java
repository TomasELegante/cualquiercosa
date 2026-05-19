package com.example.sessionservice.dto;

public record SessionCommand(
        Long userId,
        Long stationId,
        Integer durationMinutes
) {}