package org.example.crm.mappers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.entities.Ticket;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class TicketMapper {

    public static TicketDTO toTicketDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCompany().getId(),
                ticket.getCustomer().getId(),
                ticket.getAssignedUser().getId(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    public static Ticket fromCreateDTO(TicketCreateDTO dto, Company company, Customer customer, User user) {
        Ticket ticket = new Ticket();

        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setPriority(dto.priority());
        ticket.setStatus(dto.status());
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticket;
    }

    public static void updateEntity(TicketUpdateDTO dto, Ticket ticket, Company company, Customer customer, User user) {
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setPriority(dto.priority());
        ticket.setStatus(dto.status());
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setUpdatedAt(LocalDateTime.now());
    }
}
