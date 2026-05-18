package com.example.stationservice.service;

import com.example.stationservice.dto.StationCommand;
import com.example.stationservice.dto.StationResult;
import com.example.stationservice.model.Station;
import com.example.stationservice.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationResult> findAll() {
        log.debug("Obteniendo todas las estaciones");
        return stationRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public StationResult findById(Long id) {
        log.debug("Buscando estación con id: {}", id);
        return stationRepository.findById(id)
                .map(this::toResult)
                .orElseThrow(() -> {
                    log.warn("Estación no encontrada con id: {}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Estación no encontrada con id: " + id);
                });
    }

    public StationResult create(StationCommand command) {
        log.info("Creando estación: {}", command.name());
        if (stationRepository.existsByName(command.name())) {
            log.warn("Nombre de estación ya en uso: {}", command.name());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe una estación con ese nombre: " + command.name());
        }
        Station station = new Station();
        station.setName(command.name());
        station.setType(Station.StationType.valueOf(command.type()));
        station.setStatus(Station.StationStatus.valueOf(command.status()));
        station.setSpecs(command.specs());
        station.setAvailable(command.available());
        StationResult result = toResult(stationRepository.save(station));
        log.info("Estación creada exitosamente: ID={}", result.id());
        return result;
    }

    public StationResult update(Long id, StationCommand command) {
        log.info("Actualizando estación: ID={}", id);
        Station existente = stationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Estación no encontrada para actualizar: ID={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Estación no encontrada con id: " + id);
                });
        existente.setName(command.name());
        existente.setType(Station.StationType.valueOf(command.type()));
        existente.setStatus(Station.StationStatus.valueOf(command.status()));
        existente.setSpecs(command.specs());
        existente.setAvailable(command.available());
        StationResult result = toResult(stationRepository.save(existente));
        log.info("Estación actualizada exitosamente: ID={}", id);
        return result;
    }

    public void delete(Long id) {
        log.info("Eliminando estación: ID={}", id);
        if (!stationRepository.existsById(id)) {
            log.warn("Estación no encontrada para eliminar: ID={}", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Estación no encontrada con id: " + id);
        }
        stationRepository.deleteById(id);
        log.info("Estación eliminada exitosamente: ID={}", id);
    }

    public List<StationResult> findByType(Station.StationType type) {
        log.debug("Buscando estaciones por tipo: {}", type);
        return stationRepository.findByType(type).stream()
                .map(this::toResult)
                .toList();
    }

    public List<StationResult> findByStatus(Station.StationStatus status) {
        log.debug("Buscando estaciones por estado: {}", status);
        return stationRepository.findByStatus(status).stream()
                .map(this::toResult)
                .toList();
    }

    public List<StationResult> findDisponibles() {
        log.debug("Buscando estaciones disponibles");
        return stationRepository.findByAvailableTrue().stream()
                .map(this::toResult)
                .toList();
    }

    private StationResult toResult(Station station) {
        return new StationResult(
                station.getId(),
                station.getName(),
                station.getType().name(),
                station.getStatus().name(),
                station.getSpecs(),
                station.isAvailable()
        );
    }
}