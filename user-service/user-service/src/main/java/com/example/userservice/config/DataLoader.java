package com.example.userservice.config;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User u1 = new User();
            u1.setUsername("shadow99");
            u1.setEmail("shadow99@gmail.com");
            u1.setPassword(passwordEncoder.encode("player123"));
            u1.setRole(User.Role.PLAYER);

            User u2 = new User();
            u2.setUsername("nitro_king");
            u2.setEmail("nitro@arenagamer.cl");
            u2.setPassword(passwordEncoder.encode("player123"));
            u2.setRole(User.Role.PLAYER);

            User u3 = new User();
            u3.setUsername("admin_leo");
            u3.setEmail("leo@arenagamer.cl");
            u3.setPassword(passwordEncoder.encode("staff123"));
            u3.setRole(User.Role.STAFF);

            userRepository.save(u1);
            userRepository.save(u2);
            userRepository.save(u3);
        }
    }
}

