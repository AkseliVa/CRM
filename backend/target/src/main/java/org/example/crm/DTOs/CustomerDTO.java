package org.example.crm.DTOs;

import java.time.LocalDateTime;

public record CustomerDTO(
        Long id,
        Long companyId,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
