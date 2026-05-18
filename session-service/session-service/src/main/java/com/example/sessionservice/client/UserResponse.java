package com.example.sessionservice.client;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role,
        boolean active
) {}