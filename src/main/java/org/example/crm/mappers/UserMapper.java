package org.example.crm.mappers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class UserMapper {

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
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    public static void updateEntity(User user, UserUpdateDTO dto) {
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
