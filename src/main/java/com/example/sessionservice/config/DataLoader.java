package com.example.sessionservice.config;

import com.example.sessionservice.model.Session;
import com.example.sessionservice.repository.SessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final SessionRepository sessionRepository;

    public DataLoader(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) {
        // Sesión activa - usuario 1 en estación 1
        Session s1 = new Session(null, 1L, 1L,
                LocalDateTime.now().minusMinutes(45), null,
                Session.SessionStatus.ACTIVE, 60);
        sessionRepository.save(s1);

        // Sesión terminada - usuario 2 en estación 2
        Session s2 = new Session(null, 2L, 2L,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1),
                Session.SessionStatus.FINISHED, 60);
        sessionRepository.save(s2);

        // Sesión cancelada - usuario 3 en estación 3
        Session s3 = new Session(null, 3L, 3L,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().minusMinutes(10),
                Session.SessionStatus.CANCELLED, 30);
        sessionRepository.save(s3);
    }
}