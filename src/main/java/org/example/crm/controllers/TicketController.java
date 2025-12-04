package org.example.crm.controllers;

import org.example.crm.entities.Ticket;
import org.example.crm.repositories.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    };

    @GetMapping
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    };

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        ticket.setCreated_at(LocalDateTime.now());
        ticket.setUpdated_at(LocalDateTime.now());
        return ticketRepository.save(ticket);
    };

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketRepository.findById(id).orElse(null);

        if (updatedTicket == null) {
            return ResponseEntity.notFound().build();
        }

        updatedTicket.setTitle(ticket.getTitle());
        updatedTicket.setDescription(ticket.getDescription());
        updatedTicket.setPriority(ticket.getPriority());
        updatedTicket.setStatus(ticket.getStatus());
        updatedTicket.setUpdated_at(LocalDateTime.now());
        updatedTicket.setAssignedUser(ticket.getAssignedUser());
        updatedTicket.setCompany(ticket.getCompany());
        updatedTicket.setCustomer(ticket.getCustomer());

        return ResponseEntity.ok(ticketRepository.save(updatedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {

        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ticketRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
