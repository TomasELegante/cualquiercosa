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
        if (sessionRepository.count() == 0) {
            Session s1 = new Session();
            s1.setUserId(1L);
            s1.setStationId(1L);
            s1.setStartTime(LocalDateTime.now().minusMinutes(45));
            s1.setStatus(Session.SessionStatus.ACTIVE);
            s1.setDurationMinutes(60);

            Session s2 = new Session();
            s2.setUserId(2L);
            s2.setStationId(2L);
            s2.setStartTime(LocalDateTime.now().minusHours(2));
            s2.setEndTime(LocalDateTime.now().minusHours(1));
            s2.setStatus(Session.SessionStatus.FINISHED);
            s2.setDurationMinutes(60);

            Session s3 = new Session();
            s3.setUserId(3L);
            s3.setStationId(3L);
            s3.setStartTime(LocalDateTime.now().minusMinutes(30));
            s3.setEndTime(LocalDateTime.now().minusMinutes(10));
            s3.setStatus(Session.SessionStatus.CANCELLED);
            s3.setDurationMinutes(30);

            sessionRepository.save(s1);
            sessionRepository.save(s2);
            sessionRepository.save(s3);
        }
    }
}