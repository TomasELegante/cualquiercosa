package com.example.arenareservas.service;

import com.example.arenareservas.client.InventoryClient;
import com.example.arenareservas.dto.ReservaCommand;
import com.example.arenareservas.dto.ReservaResult;
import com.example.arenareservas.exception.ReservaNotFoundException;
import com.example.arenareservas.model.Reserva;
import com.example.arenareservas.model.ReservaHistory;
import com.example.arenareservas.repository.ReservaHistoryRepository;
import com.example.arenareservas.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaHistoryRepository historialRepository;
    private final InventoryClient inventoryClient;

    public ReservaService(ReservaRepository reservaRepository,
                          ReservaHistoryRepository historialRepository,
                          InventoryClient inventoryClient) {
        this.reservaRepository = reservaRepository;
        this.historialRepository = historialRepository;
        this.inventoryClient = inventoryClient;
    }

    public List<ReservaResult> listarTodas() {
        return reservaRepository.findAll().stream()
                .map(this::toResult).toList();
    }

    public List<ReservaResult> listarPorEstado(String estado) {
        return reservaRepository.findByEstadoIgnoreCase(estado).stream()
                .map(this::toResult).toList();
    }

    public ReservaResult obtenerPorId(Long id) {
        return toResult(buscarOFallar(id));
    }

    @Transactional
    public ReservaResult crear(ReservaCommand cmd) {
        if (cmd.fecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se pueden crear reservas en fechas pasadas");
        }
        validarConflictoHorario(cmd.estacionId(), cmd.fecha(),
                cmd.bloqueHorario(), -1L);

        Reserva reserva = new Reserva();
        reserva.setUsuarioId(cmd.usuarioId());
        reserva.setEstacionId(cmd.estacionId());
        reserva.setFecha(cmd.fecha());
        reserva.setBloqueHorario(cmd.bloqueHorario());
        reserva.setEstado("NUEVA");

        Reserva guardada = reservaRepository.save(reserva);

        ReservaHistory historial = new ReservaHistory();
        historial.setReserva(guardada);
        historial.setEstadoAnterior(null);
        historial.setEstadoNuevo("NUEVA");
        historial.setFechaCambio(LocalDateTime.now());
        historial.setComentario("Reserva creada");
        historialRepository.save(historial);

        return toResult(guardada);
    }

    @Transactional
    public ReservaResult actualizar(Long id, ReservaCommand cmd) {
        Reserva existente = buscarOFallar(id);
        validarConflictoHorario(cmd.estacionId(), cmd.fecha(),
                cmd.bloqueHorario(), id);
        existente.setFecha(cmd.fecha());
        existente.setBloqueHorario(cmd.bloqueHorario());
        existente.setEstacionId(cmd.estacionId());
        return toResult(reservaRepository.save(existente));
    }

    @Transactional
    public ReservaResult cambiarEstado(Long id, String nuevoEstado, String comentario) {
        Reserva reserva = buscarOFallar(id);
        String estadoAnterior = reserva.getEstado();

        reserva.setEstado(nuevoEstado.toUpperCase());
        Reserva guardada = reservaRepository.save(reserva);

        ReservaHistory historial = new ReservaHistory();
        historial.setReserva(guardada);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(nuevoEstado.toUpperCase());
        historial.setFechaCambio(LocalDateTime.now());
        historial.setComentario(comentario);
        historialRepository.save(historial);

        // Comunicación inter-servicio con RestClient
        if ("CONFIRMADA".equalsIgnoreCase(nuevoEstado)) {
            try {
                inventoryClient.actualizarStock(
                        reserva.getEstacionId(), Map.of("cantidad", -1));
                System.out.println("Stock descontado exitosamente en Inventory.");
            } catch (Exception e) {
                System.err.println("Error al comunicar con Inventory: " + e.getMessage());
            }
        }

        return toResult(guardada);
    }

    public List<ReservaHistory> obtenerHistorial(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ReservaNotFoundException(
                    "Reserva con ID " + id + " no encontrada");
        }
        return historialRepository.findByReservaIdOrderByFechaCambioDesc(id);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ReservaNotFoundException(
                    "Reserva con ID " + id + " no encontrada");
        }
        reservaRepository.deleteById(id);
    }

    private Reserva buscarOFallar(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException(
                        "Reserva con ID " + id + " no encontrada"));
    }

    private ReservaResult toResult(Reserva r) {
        return new ReservaResult(r.getId(), r.getUsuarioId(), r.getEstacionId(),
                r.getFecha(), r.getBloqueHorario(), r.getEstado());
    }

    private void validarConflictoHorario(Long estacionId, LocalDate fecha,
                                         String bloqueHorario, Long idExcluido) {
        boolean conflicto = reservaRepository
                .existsByEstacionIdAndFechaAndBloqueHorarioAndEstadoNotAndIdNot(
                        estacionId, fecha, bloqueHorario, "CANCELADA", idExcluido);
        if (conflicto) {
            throw new IllegalArgumentException(
                    "Ya existe una reserva activa para la estación " + estacionId +
                            " en la fecha " + fecha + " durante el bloque " + bloqueHorario);
        }
    }
}