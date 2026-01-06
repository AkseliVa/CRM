package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;

public record TicketUpdateDTO(
        @NotBlank(message = "Title cannot be empty")
        String title,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Priority cannot be empty")
        TicketPriority priority,

        @NotNull(message = "Status cannot be empty")
        TicketStatus status,

        @NotNull(message = "Company cannot be empty")
        Long companyId,

        @NotNull(message = "Customer cannot be empty")
        Long customerId,

        @NotNull(message = "User cannot be empty")
        Long assignedUserId
) {
}
