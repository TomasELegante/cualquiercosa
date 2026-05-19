package com.example.stationservice.controller;

import com.example.stationservice.dto.StationCommand;
import com.example.stationservice.dto.StationRequest;
import com.example.stationservice.dto.StationResponse;
import com.example.stationservice.dto.StationResult;
import com.example.stationservice.model.Station;
import com.example.stationservice.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> getAll() {
        return ResponseEntity.ok(
                stationService.findAll().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(stationService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<StationResponse> create(@Valid @RequestBody StationRequest request) {
        StationResult result = stationService.create(toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody StationRequest request) {
        return ResponseEntity.ok(toResponse(stationService.update(id, toCommand(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<StationResponse>> getByType(@PathVariable Station.StationType type) {
        return ResponseEntity.ok(
                stationService.findByType(type).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<StationResponse>> getByStatus(@PathVariable Station.StationStatus status) {
        return ResponseEntity.ok(
                stationService.findByStatus(status).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<StationResponse>> getDisponibles() {
        return ResponseEntity.ok(
                stationService.findDisponibles().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private StationCommand toCommand(StationRequest request) {
        return new StationCommand(
                request.getName(),
                request.getType(),
                request.getStatus(),
                request.getSpecs(),
                request.isAvailable()
        );
    }

    private StationResponse toResponse(StationResult result) {
        return new StationResponse(
                result.id(),
                result.name(),
                result.type(),
                result.status(),
                result.specs(),
                result.available()
        );
    }
}