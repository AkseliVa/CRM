package org.example.crm.controllers;

import org.example.crm.entities.User;
import org.example.crm.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    };

    @GetMapping("/api/user")
    public List<User> getUsers() {
        return userRepository.findAll();
    };

    @GetMapping("/api/user/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    };

    @PostMapping("/api/user")
    public User createUser(@RequestBody User user) {
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        return userRepository.save(user);
    };

    @PutMapping("/api/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userRepository.findById(id).orElse(null);

        updatedUser.setEmail(user.getEmail());
        updatedUser.setPasswordHash(user.getPasswordHash());
        updatedUser.setUpdated_at(LocalDateTime.now());

        return userRepository.save(updatedUser);
    }
}
