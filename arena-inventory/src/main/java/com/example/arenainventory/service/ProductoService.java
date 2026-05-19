package com.example.arenainventory.service;

import com.example.arenainventory.dto.ProductoCommand;
import com.example.arenainventory.dto.ProductoResult;
import com.example.arenainventory.exception.ProductoNotFoundException;
import com.example.arenainventory.model.Producto;
import com.example.arenainventory.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoResult> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toResult).toList();
    }

    public List<ProductoResult> listarPorCategoria(String categoria) {
        try {
            Producto.Categoria cat = Producto.Categoria.valueOf(categoria.toUpperCase());
            return productoRepository.findByCategoria(cat).stream()
                    .map(this::toResult).toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Categoría inválida. Use: CONSOLA, PERIFERICO o JUEGO");
        }
    }

    public ProductoResult obtenerPorId(Long id) {
        return toResult(buscarOFallar(id));
    }

    public ProductoResult crear(ProductoCommand cmd) {
        if (cmd.stock() != null && cmd.stock() < 0) {
            throw new IllegalArgumentException(
                    "El stock inicial no puede ser negativo");
        }
        Producto producto = new Producto();
        producto.setNombre(cmd.nombre());
        producto.setCategoria(cmd.categoria());
        producto.setStock(cmd.stock());
        producto.setPrecioAlquiler(cmd.precioAlquiler());
        return toResult(productoRepository.save(producto));
    }

    public ProductoResult actualizar(Long id, ProductoCommand cmd) {
        Producto existente = buscarOFallar(id);
        existente.setNombre(cmd.nombre());
        existente.setCategoria(cmd.categoria());
        existente.setStock(cmd.stock());
        existente.setPrecioAlquiler(cmd.precioAlquiler());
        return toResult(productoRepository.save(existente));
    }

    public ProductoResult actualizarStock(Long id, Integer cantidad) {
        if (cantidad == null || cantidad == 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser distinta de cero");
        }
        Producto producto = buscarOFallar(id);
        int nuevoStock = producto.getStock() + cantidad;

        if (nuevoStock < 0) {
            throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + producto.getStock());
        }
        producto.setStock(nuevoStock);
        return toResult(productoRepository.save(producto));
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException(
                    "No se puede eliminar: ID " + id + " no existe");
        }
        productoRepository.deleteById(id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private Producto buscarOFallar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(
                        "Producto con ID " + id + " no encontrado"));
    }

    private ProductoResult toResult(Producto p) {
        return new ProductoResult(
                p.getId(), p.getNombre(), p.getCategoria(),
                p.getStock(), p.getPrecioAlquiler());
    }
}