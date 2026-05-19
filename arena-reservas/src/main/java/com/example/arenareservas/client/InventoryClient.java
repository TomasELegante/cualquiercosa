package com.example.arenareservas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9001/api/v1/productos")
                .build();
    }

    public void actualizarStock(Long productoId, Map<String, Integer> body) {
        restClient.patch()
                .uri("/{id}/stock", productoId)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}