package org.example.crm.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public List<TicketDTO> getTickets() {
        return ticketService.getAllTickets();
    };

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    };

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket (@Valid @RequestBody TicketCreateDTO dto) {
        TicketDTO created = ticketService.createTicket(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
