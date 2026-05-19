package com.example.userservice.dto;

public record UserCommand(
        String username,
        String email,
        String role
) {}