package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerNoteCreateDTO(
        @NotNull(message = "Note should have a customer")
        Long customerId,

        @NotNull(message = "Note should have a user")
        Long userId,

        @NotBlank(message = "Note should have content")
        String content
) {
}
