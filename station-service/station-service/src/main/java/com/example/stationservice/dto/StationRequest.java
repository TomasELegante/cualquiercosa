package com.example.stationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StationRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El tipo no puede ser nulo")
    private String type;

    @NotNull(message = "El estado no puede ser nulo")
    private String status;

    @NotBlank(message = "Las especificaciones no pueden estar vacías")
    private String specs;

    private boolean available = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}