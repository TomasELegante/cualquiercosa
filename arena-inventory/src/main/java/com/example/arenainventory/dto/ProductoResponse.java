package com.example.arenainventory.dto;

import com.example.arenainventory.model.Producto;

public record ProductoResponse(
        Long id,
        String nombre,
        Producto.Categoria categoria,
        Integer stock,
        Double precioAlquiler
) {}