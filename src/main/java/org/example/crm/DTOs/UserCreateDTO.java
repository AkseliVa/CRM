package org.example.crm.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.crm.enums.UserRole;

public record UserCreateDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank
        @Size(min = 8, message = "Password must be at leat 8 characters")
        String password,

        @NotNull(message = "User should have a role")
        UserRole role
) {
}
