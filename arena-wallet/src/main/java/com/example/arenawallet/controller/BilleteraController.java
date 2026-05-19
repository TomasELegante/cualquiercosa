package com.example.arenawallet.controller;

import com.example.arenawallet.dto.BilleteraCommand;
import com.example.arenawallet.dto.BilleteraRequest;
import com.example.arenawallet.dto.BilleteraResponse;
import com.example.arenawallet.dto.BilleteraResult;
import com.example.arenawallet.service.BilleteraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/billeteras")
@Validated
public class BilleteraController {

    private final BilleteraService billeteraService;

    public BilleteraController(BilleteraService billeteraService) {
        this.billeteraService = billeteraService;
    }

    @GetMapping
    public ResponseEntity<List<BilleteraResponse>> listar() {
        return ResponseEntity.ok(
                billeteraService.listarTodas().stream()
                        .map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BilleteraResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(billeteraService.obtenerPorId(id)));
    }

    @PostMapping
    public ResponseEntity<BilleteraResponse> crear(
            @Valid @RequestBody BilleteraRequest request) {
        BilleteraCommand cmd = new BilleteraCommand(
                request.idUsuario(), request.saldo(), request.puntosFidelizacion());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(billeteraService.crear(cmd)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BilleteraResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BilleteraRequest request) {
        BilleteraCommand cmd = new BilleteraCommand(
                request.idUsuario(), request.saldo(), request.puntosFidelizacion());
        return ResponseEntity.ok(toResponse(billeteraService.actualizar(id, cmd)));
    }

    @PatchMapping("/{id}/recargas")
    public ResponseEntity<BilleteraResponse> recargarSaldo(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {
        if (body == null || !body.containsKey("monto")) {
            throw new IllegalArgumentException("El campo 'monto' es obligatorio");
        }
        Double monto = body.get("monto");
        if (monto == null || monto <= 0) {
            throw new IllegalArgumentException(
                    "El monto a recargar debe ser positivo");
        }
        return ResponseEntity.ok(toResponse(billeteraService.recargarSaldo(id, monto)));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(billeteraService.obtenerHistorial(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        billeteraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private BilleteraResponse toResponse(BilleteraResult r) {
        return new BilleteraResponse(
                r.id(), r.idUsuario(), r.saldo(), r.puntosFidelizacion());
    }
}