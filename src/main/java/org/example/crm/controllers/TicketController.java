package org.example.crm.controllers;

import org.example.crm.entities.Ticket;
import org.example.crm.repositories.TicketRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    };

    @GetMapping("/api/ticket")
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    };

    @GetMapping("/api/ticket/{id}")
    public Optional<Ticket> getTicket(@PathVariable Long id) {
        return ticketRepository.findById(id);
    };

    @PostMapping("/api/ticket")
    public Ticket createTicket(@RequestBody Ticket ticket) {
        ticket.setCreated_at(LocalDateTime.now());
        ticket.setUpdated_at(LocalDateTime.now());
        return ticketRepository.save(ticket);
    };

    @PutMapping("/api/ticket/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketRepository.findById(id).orElse(null);

        updatedTicket.setTitle(ticket.getTitle());
        updatedTicket.setDescription(ticket.getDescription());
        updatedTicket.setPriority(ticket.getPriority());
        updatedTicket.setStatus(ticket.getStatus());
        updatedTicket.setUpdated_at(ticket.getUpdated_at());
        updatedTicket.setAssignedUser(ticket.getAssignedUser());
        updatedTicket.setCompany(ticket.getCompany());
        updatedTicket.setCustomer(ticket.getCustomer());

        return ticketRepository.save(updatedTicket);
    }
}
