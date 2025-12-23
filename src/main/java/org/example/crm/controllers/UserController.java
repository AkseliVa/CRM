package org.example.crm.controllers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;
import org.example.crm.mappers.UserMapper;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    };

    @GetMapping
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(UserMapper.toUserDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO dto) {
        User user = UserMapper.fromCreateDTO(dto);

        User saved = userRepository.save(user);

        return ResponseEntity.ok(UserMapper.toUserDTO(saved));
    };

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        UserMapper.updateEntity(existing, dto);

        User saved = userRepository.save(existing);

        return ResponseEntity.ok(UserMapper.toUserDTO(saved));
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
