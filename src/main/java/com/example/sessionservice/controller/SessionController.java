package com.example.sessionservice.controller;

import com.example.sessionservice.model.Session;
import com.example.sessionservice.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAll() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Session> startSession(@Valid @RequestBody Session session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.startSession(session));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Session> finishSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.finishSession(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Session> cancelSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.cancelSession(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Session>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(sessionService.findByUserId(userId));
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<Session>> getByStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(sessionService.findByStationId(stationId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Session>> getActivas() {
        return ResponseEntity.ok(sessionService.findActivas());
    }
}