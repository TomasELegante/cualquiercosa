package com.example.userservice.service;

import com.example.userservice.dto.UserCommand;
import com.example.userservice.dto.UserResult;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResult> findAll() {
        log.debug("Obteniendo todos los usuarios");
        return userRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public UserResult findById(Long id) {
        log.debug("Buscando usuario con id: {}", id);
        return userRepository.findById(id)
                .map(this::toResult)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id);
                });
    }

    public UserResult create(UserCommand command) {
        log.info("Creando usuario: {}", command.username());
        if (userRepository.existsByUsername(command.username())) {
            log.warn("Username ya en uso: {}", command.username());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El username ya está en uso: " + command.username());
        }
        if (userRepository.existsByEmail(command.email())) {
            log.warn("Email ya registrado: {}", command.email());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado: " + command.email());
        }
        User user = new User();
        user.setUsername(command.username());
        user.setEmail(command.email());
        user.setRole(User.Role.valueOf(command.role()));
        user.setActive(true);
        UserResult result = toResult(userRepository.save(user));
        log.info("Usuario creado exitosamente: ID={}", result.id());
        return result;
    }

    public UserResult update(Long id, UserCommand command) {
        log.info("Actualizando usuario: ID={}", id);
        User existente = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado para actualizar: ID={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id);
                });
        existente.setUsername(command.username());
        existente.setEmail(command.email());
        existente.setRole(User.Role.valueOf(command.role()));
        UserResult result = toResult(userRepository.save(existente));
        log.info("Usuario actualizado exitosamente: ID={}", id);
        return result;
    }

    public void delete(Long id) {
        log.info("Eliminando usuario: ID={}", id);
        if (!userRepository.existsById(id)) {
            log.warn("Usuario no encontrado para eliminar: ID={}", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Usuario eliminado exitosamente: ID={}", id);
    }

    public List<UserResult> findByRole(User.Role role) {
        log.debug("Buscando usuarios con rol: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::toResult)
                .toList();
    }

    public List<UserResult> findActivos() {
        log.debug("Buscando usuarios activos");
        return userRepository.findByActiveTrue().stream()
                .map(this::toResult)
                .toList();
    }

    private UserResult toResult(User user) {
        return new UserResult(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive()
        );
    }
}