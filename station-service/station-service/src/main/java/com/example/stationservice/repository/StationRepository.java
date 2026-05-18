package com.example.stationservice.repository;

import com.example.stationservice.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long>
{
    List<Station> findByType(Station.StationType type);
    List<Station> findByStatus(Station.StationStatus status);
    List<Station> findByAvailableTrue();
    boolean existsByName(String name);
}
