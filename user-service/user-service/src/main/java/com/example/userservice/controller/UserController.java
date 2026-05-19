package com.example.userservice.controller;

import com.example.userservice.dto.UserCommand;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserResult;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        log.debug("GET /api/users");
        return ResponseEntity.ok(
                userService.findAll().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or @userSecurity.isOwner(#id, authentication)")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        log.debug("GET /api/users/{}", id);
        return ResponseEntity.ok(toResponse(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        log.info("POST /api/users - username: {}", request.getUsername());
        UserResult result = userService.create(toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserRequest request) {
        log.info("PUT /api/users/{}", id);
        return ResponseEntity.ok(toResponse(userService.update(id, toCommand(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/users/{}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getByRole(@PathVariable User.Role role) {
        log.debug("GET /api/users/role/{}", role);
        return ResponseEntity.ok(
                userService.findByRole(role).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponse>> getActivos() {
        log.debug("GET /api/users/active");
        return ResponseEntity.ok(
                userService.findActivos().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private UserCommand toCommand(UserRequest request) {
        return new UserCommand(
                request.getUsername(),
                request.getEmail(),
                request.getRole()
        );
    }

    private UserResponse toResponse(UserResult result) {
        return new UserResponse(
                result.id(),
                result.username(),
                result.email(),
                result.role(),
                result.active()
        );
    }
}