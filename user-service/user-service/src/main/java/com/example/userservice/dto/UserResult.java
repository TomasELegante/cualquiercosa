package com.example.userservice.dto;

public record UserResult(
        Long id,
        String username,
        String email,
        String role,
        boolean active
) {}