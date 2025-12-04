package org.example.crm.DTOs;

public record CustomerNotesCreateDTO(
        Long customerId,
        Long userId,
        String content
) {
}
