package com.example.arenawallet.model;

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
@Table(name = "billetera_history")
public class BilleteraHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billetera_id", nullable = false)
    @JsonIgnore
    private Billetera billetera;

    private String tipoOperacion; // Ej: "RECARGA", "PAGO", "CREACION"

    private Double montoInvolucrado;

    private Double saldoAnterior;

    private Double saldoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaOperacion;
}