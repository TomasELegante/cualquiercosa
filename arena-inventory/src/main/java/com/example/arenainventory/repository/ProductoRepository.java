package com.example.arenainventory.repository;

import com.example.arenainventory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Filtrar por categoría (ya lo tenías, está muy bien)
    List<Producto> findByCategoria(Producto.Categoria categoria);

    // Buscar por nombre exacto (útil para validaciones de duplicidad)
    Optional<Producto> findByNombre(String nombre);

    // Verificar si existe por nombre (más rápido que buscar el objeto completo)
    boolean existsByNombre(String nombre);
}