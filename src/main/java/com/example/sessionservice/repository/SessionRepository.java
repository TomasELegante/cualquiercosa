package com.example.sessionservice.repository;

import com.example.sessionservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUserId(Long userId);

    List<Session> findByStationId(Long stationId);

    List<Session> findByStatus(Session.SessionStatus status);

    Optional<Session> findByStationIdAndStatus(Long stationId, Session.SessionStatus status);

    Optional<Session> findByUserIdAndStatus(Long userId, Session.SessionStatus status);
}