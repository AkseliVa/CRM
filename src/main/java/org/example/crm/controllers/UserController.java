package org.example.crm.controllers;

import org.example.crm.entities.User;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    };

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    };

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        return userRepository.save(user);
    };

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userRepository.findById(id).orElse(null);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        updatedUser.setEmail(user.getEmail());
        updatedUser.setPasswordHash(user.getPasswordHash());
        updatedUser.setUpdated_at(LocalDateTime.now());

        return ResponseEntity.ok(userRepository.save(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
