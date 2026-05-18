package com.example.sessionservice.dto;

import jakarta.validation.constraints.NotNull;

public class SessionRequest {

    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El ID de estación no puede ser nulo")
    private Long stationId;

    @NotNull(message = "La duración no puede ser nula")
    private Integer durationMinutes;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}