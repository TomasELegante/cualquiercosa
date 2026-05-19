package com.example.arenareservas.repository;

import com.example.arenareservas.model.ReservaHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaHistoryRepository extends JpaRepository<ReservaHistory, Long> {

    // Método clave para el GET /history del Controller
    List<ReservaHistory> findByReservaIdOrderByFechaCambioDesc(Long reservaId);
}