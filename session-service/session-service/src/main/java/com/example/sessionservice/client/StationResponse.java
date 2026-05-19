package com.example.sessionservice.client;

public record StationResponse(
        Long id,
        String name,
        String type,
        String status,
        String specs,
        boolean available
) {}