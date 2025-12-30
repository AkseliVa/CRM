package org.example.crm.DTOs;

public record CustomerNoteUpdateDTO(
        Long customerId,
        Long userId,
        String content
) {
}
