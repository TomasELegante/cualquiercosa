package com.example.arenareservas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva_history")
public class ReservaHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la Reserva original (Lazy para no sobrecargar consultas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonIgnore // Para evitar bucles infinitos al devolver el JSON
    private Reserva reserva;

    private String estadoAnterior;

    private String estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    private String comentario;
}