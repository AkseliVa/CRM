package org.example.crm.controllers;

import org.example.crm.entities.User;
import org.example.crm.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

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
        return userRepository.save(user);
    };
}
