package org.example.crm.DTOs;

public record CustomerNotesUpdateDTO(
        Long customerId,
        Long userId,
        String content
) {
}
