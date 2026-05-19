package com.example.arenareservas.controller;

import com.example.arenareservas.dto.ReservaCommand;
import com.example.arenareservas.dto.ReservaRequest;
import com.example.arenareservas.dto.ReservaResponse;
import com.example.arenareservas.dto.ReservaResult;
import com.example.arenareservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listar(
            @RequestParam(required = false) String estado) {
        List<ReservaResult> resultado = (estado != null && !estado.isBlank())
                ? reservaService.listarPorEstado(estado)
                : reservaService.listarTodas();
        return ResponseEntity.ok(resultado.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(reservaService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<?>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerHistorial(id));
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(
            @Valid @RequestBody ReservaRequest request) {
        ReservaCommand cmd = new ReservaCommand(
                request.usuarioId(), request.estacionId(),
                request.fecha(), request.bloqueHorario());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(reservaService.crear(cmd)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequest request) {
        ReservaCommand cmd = new ReservaCommand(
                request.usuarioId(), request.estacionId(),
                request.fecha(), request.bloqueHorario());
        return ResponseEntity.ok(toResponse(reservaService.actualizar(id, cmd)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReservaResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El campo 'estado' es obligatorio");
        }
        String comentario = body.get("comentario");
        return ResponseEntity.ok(
                toResponse(reservaService.cambiarEstado(id, nuevoEstado, comentario)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ReservaResponse toResponse(ReservaResult r) {
        return new ReservaResponse(
                r.id(), r.usuarioId(), r.estacionId(),
                r.fecha(), r.bloqueHorario(), r.estado());
    }
}