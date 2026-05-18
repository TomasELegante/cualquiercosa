package com.example.sessionservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@Slf4j
public class StationClient {

    private final RestClient restClient;

    public StationClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8083")
                .build();
    }

    public Optional<StationResponse> findById(Long stationId) {
        try {
            StationResponse response = restClient.get()
                    .uri("/api/stations/" + stationId)
                    .retrieve()
                    .body(StationResponse.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Estación no encontrada con id: {}", stationId);
            return Optional.empty();
        }
    }

    public boolean existsStation(Long stationId) {
        return findById(stationId).isPresent();
    }
}