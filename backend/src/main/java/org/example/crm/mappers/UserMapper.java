package org.example.crm.mappers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class UserMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User fromCreateDTO(UserCreateDTO dto) {
        User user = new User();

        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setPasswordHash(encoder.encode(dto.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    public static void updateEntity(User existing, UserUpdateDTO dto) {
        existing.setEmail(dto.email());
        existing.setRole(dto.role());

        if (dto.password() != null && !dto.password().isBlank()) {
            existing.setPasswordHash(encoder.encode(dto.password()));
        }

        existing.setUpdatedAt(LocalDateTime.now());
    }
}
