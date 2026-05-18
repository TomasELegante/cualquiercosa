package com.example.sessionservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@Slf4j
public class UserClient {

    private final RestClient restClient;

    public UserClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .defaultHeader("Authorization", "Basic YWRtaW5fbGVvOnN0YWZmMTIz")
                .build();
    }

    public Optional<UserResponse> findById(Long userId) {
        try {
            UserResponse response = restClient.get()
                    .uri("/api/users/" + userId)
                    .retrieve()
                    .body(UserResponse.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Usuario no encontrado con id: {}", userId);
            return Optional.empty();
        }
    }

    public boolean existsUser(Long userId) {
        return findById(userId).isPresent();
    }
}