package org.example.crm.controllers;

import org.example.crm.entities.Ticket;
import org.example.crm.repositories.TicketRepository;
import org.springframework.web.bind.annotation.*;

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
        return ticketRepository.save(ticket);
    };
}
