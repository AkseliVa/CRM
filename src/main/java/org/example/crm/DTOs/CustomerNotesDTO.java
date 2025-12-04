package org.example.crm.DTOs;

import java.time.LocalDateTime;

public record CustomerNotesDTO(
    Long id,
    Long customerId,
    Long userId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
