package com.example.arenainventory.controller;

import com.example.arenainventory.dto.ProductoCommand;
import com.example.arenainventory.dto.ProductoRequest;
import com.example.arenainventory.dto.ProductoResponse;
import com.example.arenainventory.dto.ProductoResult;
import com.example.arenainventory.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) String categoria) {
        List<ProductoResult> resultado = (categoria != null && !categoria.isBlank())
                ? productoService.listarPorCategoria(categoria)
                : productoService.listarTodos();
        return ResponseEntity.ok(resultado.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productoService.obtenerPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody ProductoRequest request) {
        ProductoCommand cmd = new ProductoCommand(
                request.nombre(), request.categoria(),
                request.stock(), request.precioAlquiler());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(productoService.crear(cmd)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        ProductoCommand cmd = new ProductoCommand(
                request.nombre(), request.categoria(),
                request.stock(), request.precioAlquiler());
        return ResponseEntity.ok(toResponse(productoService.actualizar(id, cmd)));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        if (body == null || !body.containsKey("cantidad")) {
            throw new IllegalArgumentException("El campo 'cantidad' es obligatorio");
        }
        return ResponseEntity.ok(
                toResponse(productoService.actualizarStock(id, body.get("cantidad"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ProductoResponse toResponse(ProductoResult r) {
        return new ProductoResponse(
                r.id(), r.nombre(), r.categoria(),
                r.stock(), r.precioAlquiler());
    }
}