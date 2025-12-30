package org.example.crm.DTOs;

import java.time.LocalDateTime;

public record CustomerNoteDTO(
    Long id,
    Long customerId,
    Long userId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
