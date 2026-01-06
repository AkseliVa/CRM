package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerNoteUpdateDTO(
        @NotNull(message = "Customer cannot be empty")
        Long customerId,

        @NotNull(message = "User cannot be empty")
        Long userId,

        @NotBlank(message = "Content cannot be empty")
        String content
) {
}
