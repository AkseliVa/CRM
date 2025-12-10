package org.example.crm.controllers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Ticket;
import org.example.crm.mappers.TicketMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.TicketRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, CompanyRepository companyRepository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    };

    @GetMapping
    public List<TicketDTO> getTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toTicketDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(t -> ResponseEntity.ok(TicketMapper.toTicketDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket (@RequestBody TicketCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                .orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        var customer = customerRepository.findById(dto.customerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.assignedUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = TicketMapper.fromCreateDTO(dto, company, customer, user);

        Ticket saved = ticketRepository.save(ticket);

        return ResponseEntity.ok(TicketMapper.toTicketDTO(saved));
    };

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketUpdateDTO dto) {
        Ticket existing = ticketRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        var company = companyRepository.findById(dto.companyId())
                .orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        var customer = customerRepository.findById(dto.customerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.assignedUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        TicketMapper.updateEntity(dto, existing, company, customer, user);

        Ticket saved = ticketRepository.save(existing);

        return ResponseEntity.ok(TicketMapper.toTicketDTO(saved));
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
