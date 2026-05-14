package com.example.sessionservice.service;

import com.example.sessionservice.model.Session;
import com.example.sessionservice.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sesión no encontrada con id: " + id));
    }

    public Session startSession(Session session) {
        // Verificar que la estación no tenga una sesión activa
        sessionRepository.findByStationIdAndStatus(session.getStationId(), Session.SessionStatus.ACTIVE)
                .ifPresent(s -> { throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "La estación ya tiene una sesión activa"); });

        // Verificar que el usuario no tenga una sesión activa
        sessionRepository.findByUserIdAndStatus(session.getUserId(), Session.SessionStatus.ACTIVE)
                .ifPresent(s -> { throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "El usuario ya tiene una sesión activa"); });

        session.setStartTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.ACTIVE);
        return sessionRepository.save(session);
    }

    public Session finishSession(Long id) {
        Session session = findById(id);
        if (session.getStatus() != Session.SessionStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La sesión no está activa");
        }
        session.setEndTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.FINISHED);
        return sessionRepository.save(session);
    }

    public Session cancelSession(Long id) {
        Session session = findById(id);
        if (session.getStatus() != Session.SessionStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La sesión no está activa");
        }
        session.setEndTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.CANCELLED);
        return sessionRepository.save(session);
    }

    public List<Session> findByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    public List<Session> findByStationId(Long stationId) {
        return sessionRepository.findByStationId(stationId);
    }

    public List<Session> findActivas() {
        return sessionRepository.findByStatus(Session.SessionStatus.ACTIVE);
    }
}