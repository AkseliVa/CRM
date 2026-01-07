package org.example.crm.DTOs;

import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketDTO(
        Long id,
        String title,
        String description,
        TicketPriority priority,
        TicketStatus status,
        Long companyId,
        Long customerId,
        Long assignedUserId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
