package com.example.arenainventory.dto;

import com.example.arenainventory.model.Producto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequest(

        @NotBlank(message = "El nombre del producto es obligatorio")
        String nombre,

        @NotNull(message = "La categoría es obligatoria")
        Producto.Categoria categoria,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "El precio de alquiler es obligatorio")
        @Min(value = 0, message = "El precio no puede ser negativo")
        Double precioAlquiler
) {}