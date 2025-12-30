package org.example.crm.DTOs;

public record CustomerNoteCreateDTO(
        Long customerId,
        Long userId,
        String content
) {
}
