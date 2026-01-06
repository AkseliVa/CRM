package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;

public record TicketCreateDTO(
        @NotBlank(message = "Ticket should have a title")
        String title,

        @NotBlank(message = "Ticket should have a description")
        String description,

        @NotNull(message = "Ticket should have a priority")
        TicketPriority priority,

        @NotNull(message = "Ticket should have a status")
        TicketStatus status,

        @NotNull(message = "Ticket should have a company")
        Long companyId,

        @NotNull(message = "Ticket should have a customer")
        Long customerId,

        @NotNull(message = "Ticket should have a user")
        Long assignedUserId
) {
}
