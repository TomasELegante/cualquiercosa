package com.example.arenareservas.repository;

import com.example.arenareservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Filtrar por estado
    List<Reserva> findByEstadoIgnoreCase(String estado);

    // Verificar conflicto de horario (misma estación, misma fecha, mismo bloque, no cancelada)
    boolean existsByEstacionIdAndFechaAndBloqueHorarioAndEstadoNotAndIdNot(
            Long estacionId,
            java.time.LocalDate fecha,
            String bloqueHorario,
            String estado,
            Long id);
}