package com.example.sessionservice.service;

import com.example.sessionservice.client.StationClient;
import com.example.sessionservice.client.StationResponse;
import com.example.sessionservice.client.UserClient;
import com.example.sessionservice.client.UserResponse;
import com.example.sessionservice.dto.SessionCommand;
import com.example.sessionservice.dto.SessionResult;
import com.example.sessionservice.model.Session;
import com.example.sessionservice.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserClient userClient;
    private final StationClient stationClient;

    public SessionService(SessionRepository sessionRepository,
                          UserClient userClient,
                          StationClient stationClient) {
        this.sessionRepository = sessionRepository;
        this.userClient = userClient;
        this.stationClient = stationClient;
    }

    public List<SessionResult> findAll() {
        log.debug("Obteniendo todas las sesiones");
        return sessionRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public SessionResult findById(Long id) {
        log.debug("Buscando sesión con id: {}", id);
        return sessionRepository.findById(id)
                .map(this::toResult)
                .orElseThrow(() -> {
                    log.warn("Sesión no encontrada con id: {}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Sesión no encontrada con id: " + id);
                });
    }

    public SessionResult startSession(SessionCommand command) {
        log.info("Iniciando sesión - userId: {}, stationId: {}", command.userId(), command.stationId());

        UserResponse user = userClient.findById(command.userId())
                .orElseThrow(() -> {
                    log.warn("Usuario no existe: ID={}", command.userId());
                    return new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "El usuario con id " + command.userId() + " no existe");
                });

        StationResponse station = stationClient.findById(command.stationId())
                .orElseThrow(() -> {
                    log.warn("Estación no existe: ID={}", command.stationId());
                    return new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "La estación con id " + command.stationId() + " no existe");
                });

        sessionRepository.findByStationIdAndStatus(command.stationId(), Session.SessionStatus.ACTIVE)
                .ifPresent(s -> {
                    log.warn("Estación ya tiene sesión activa: ID={}", command.stationId());
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "La estación ya tiene una sesión activa");
                });

        sessionRepository.findByUserIdAndStatus(command.userId(), Session.SessionStatus.ACTIVE)
                .ifPresent(s -> {
                    log.warn("Usuario ya tiene sesión activa: ID={}", command.userId());
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "El usuario ya tiene una sesión activa");
                });

        Session session = new Session();
        session.setUserId(command.userId());
        session.setUsername(user.username());
        session.setStationId(command.stationId());
        session.setStationName(station.name());
        session.setStartTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.ACTIVE);
        session.setDurationMinutes(command.durationMinutes());
        SessionResult result = toResult(sessionRepository.save(session));
        log.info("Sesión iniciada exitosamente: ID={}", result.id());
        return result;
    }

    public SessionResult finishSession(Long id) {
        log.info("Finalizando sesión: ID={}", id);
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sesión no encontrada: ID={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Sesión no encontrada con id: " + id);
                });
        if (session.getStatus() != Session.SessionStatus.ACTIVE) {
            log.warn("Sesión no está activa: ID={}", id);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La sesión no está activa");
        }
        session.setEndTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.FINISHED);
        SessionResult result = toResult(sessionRepository.save(session));
        log.info("Sesión finalizada exitosamente: ID={}", id);
        return result;
    }

    public SessionResult cancelSession(Long id) {
        log.info("Cancelando sesión: ID={}", id);
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sesión no encontrada: ID={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Sesión no encontrada con id: " + id);
                });
        if (session.getStatus() != Session.SessionStatus.ACTIVE) {
            log.warn("Sesión no está activa: ID={}", id);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La sesión no está activa");
        }
        session.setEndTime(LocalDateTime.now());
        session.setStatus(Session.SessionStatus.CANCELLED);
        SessionResult result = toResult(sessionRepository.save(session));
        log.info("Sesión cancelada exitosamente: ID={}", id);
        return result;
    }

    public List<SessionResult> findByUserId(Long userId) {
        log.debug("Buscando sesiones por usuario: ID={}", userId);
        return sessionRepository.findByUserId(userId).stream()
                .map(this::toResult)
                .toList();
    }

    public List<SessionResult> findByStationId(Long stationId) {
        log.debug("Buscando sesiones por estación: ID={}", stationId);
        return sessionRepository.findByStationId(stationId).stream()
                .map(this::toResult)
                .toList();
    }

    public List<SessionResult> findActivas() {
        log.debug("Buscando sesiones activas");
        return sessionRepository.findByStatus(Session.SessionStatus.ACTIVE).stream()
                .map(this::toResult)
                .toList();
    }

    private SessionResult toResult(Session session) {
        return new SessionResult(
                session.getId(),
                session.getUserId(),
                session.getUsername(),
                session.getStationId(),
                session.getStationName(),
                session.getStartTime(),
                session.getEndTime(),
                session.getStatus().name(),
                session.getDurationMinutes()
        );
    }
}