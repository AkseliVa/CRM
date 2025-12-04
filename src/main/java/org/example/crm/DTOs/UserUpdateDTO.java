package org.example.crm.DTOs;

import org.example.crm.enums.UserRole;

public record UserUpdateDTO(
        String email,
        String password,
        UserRole role
) {
}
