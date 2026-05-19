package com.example.arenareservas.config;

import com.example.arenareservas.model.Reserva;
import com.example.arenareservas.repository.ReservaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ReservaRepository reservaRepository;

    public DataInitializer(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) {
        if (reservaRepository.count() == 0) {
            reservaRepository.save(new Reserva(null, 1L, 1L,
                    LocalDate.now(), "10:00-11:00", "CONFIRMADA"));
            reservaRepository.save(new Reserva(null, 2L, 2L,
                    LocalDate.now(), "12:00-13:00", "PENDIENTE"));
            reservaRepository.save(new Reserva(null, 3L, 3L,
                    LocalDate.now().plusDays(1), "14:00-15:00", "CONFIRMADA"));
            System.out.println(">>> Datos iniciales de reservas cargados.");
        }
    }
}