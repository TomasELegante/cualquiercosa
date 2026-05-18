package com.example.sessionservice.controller;

import com.example.sessionservice.dto.SessionCommand;
import com.example.sessionservice.dto.SessionRequest;
import com.example.sessionservice.dto.SessionResponse;
import com.example.sessionservice.dto.SessionResult;
import com.example.sessionservice.service.SessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAll() {
        log.debug("GET /api/sessions");
        return ResponseEntity.ok(
                sessionService.findAll().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getById(@PathVariable Long id) {
        log.debug("GET /api/sessions/{}", id);
        return ResponseEntity.ok(toResponse(sessionService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<SessionResponse> startSession(@Valid @RequestBody SessionRequest request) {
        log.info("POST /api/sessions - userId: {}", request.getUserId());
        SessionResult result = sessionService.startSession(toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<SessionResponse> finishSession(@PathVariable Long id) {
        log.info("PUT /api/sessions/{}/finish", id);
        return ResponseEntity.ok(toResponse(sessionService.finishSession(id)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<SessionResponse> cancelSession(@PathVariable Long id) {
        log.info("PUT /api/sessions/{}/cancel", id);
        return ResponseEntity.ok(toResponse(sessionService.cancelSession(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionResponse>> getByUser(@PathVariable Long userId) {
        log.debug("GET /api/sessions/user/{}", userId);
        return ResponseEntity.ok(
                sessionService.findByUserId(userId).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<SessionResponse>> getByStation(@PathVariable Long stationId) {
        log.debug("GET /api/sessions/station/{}", stationId);
        return ResponseEntity.ok(
                sessionService.findByStationId(stationId).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<SessionResponse>> getActivas() {
        log.debug("GET /api/sessions/active");
        return ResponseEntity.ok(
                sessionService.findActivas().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private SessionCommand toCommand(SessionRequest request) {
        return new SessionCommand(
                request.getUserId(),
                request.getStationId(),
                request.getDurationMinutes()
        );
    }

    private SessionResponse toResponse(SessionResult result) {
        return new SessionResponse(
                result.id(),
                result.userId(),
                result.username(),
                result.stationId(),
                result.stationName(),
                result.startTime(),
                result.endTime(),
                result.status(),
                result.durationMinutes()
        );
    }
}