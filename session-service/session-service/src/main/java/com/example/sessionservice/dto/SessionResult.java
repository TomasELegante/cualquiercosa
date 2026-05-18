package com.example.sessionservice.dto;

import java.time.LocalDateTime;

public record SessionResult(
        Long id,
        Long userId,
        String username,
        Long stationId,
        String stationName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status,
        Integer durationMinutes
) {}