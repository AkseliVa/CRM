package org.example.crm.DTOs;

import org.example.crm.enums.UserRole;

public record UserCreateDTO(
        String email,
        String password,
        UserRole role
) {
}
