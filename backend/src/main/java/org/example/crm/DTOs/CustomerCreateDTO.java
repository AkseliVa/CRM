package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerCreateDTO(
        @NotBlank(message = "Customer should have a name")
        String name,

        @NotBlank(message = "Customer should have an email")
        String email,

        @NotBlank(message = "Customer should have a phone number")
        String phone,

        @NotNull(message = "Customer should have a company")
        Long companyId
) {
}
