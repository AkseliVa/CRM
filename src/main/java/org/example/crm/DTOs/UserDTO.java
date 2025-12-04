package org.example.crm.DTOs;

import org.example.crm.enums.UserRole;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
