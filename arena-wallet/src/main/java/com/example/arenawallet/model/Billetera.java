package com.example.arenawallet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billeteras")
public class Billetera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    @Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
    @Column(nullable = false, unique = true)
    private Long idUsuario;

    @NotNull(message = "El saldo es obligatorio")
    @PositiveOrZero(message = "El saldo no puede ser negativo")
    @Column(nullable = false) // <-- Eliminamos precision y scale
    private Double saldo;

    @NotNull(message = "Los puntos de fidelización son obligatorios")
    @PositiveOrZero(message = "Los puntos no pueden ser negativos")
    @Column(nullable = false)
    private Integer puntosFidelizacion;
}