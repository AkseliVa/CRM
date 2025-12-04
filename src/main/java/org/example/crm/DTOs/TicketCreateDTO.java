package org.example.crm.DTOs;

import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;

public record TicketCreateDTO(
        String title,
        String description,
        TicketPriority priority,
        TicketStatus status,
        Long companyId,
        Long customerId,
        Long assignedUserId
) {
}
