package org.example.crm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Ticket;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.mappers.TicketMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.TicketRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAllWithRelations().stream()
                .map(TicketMapper::toTicketDTO)
                .toList();
    }

    public TicketDTO getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toTicketDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Transactional
    public TicketDTO createTicket(TicketCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Ticket ticket = TicketMapper.fromCreateDTO(dto, company, customer, user);
        Ticket saved = ticketRepository.save(ticket);

        return TicketMapper.toTicketDTO(saved);
    }

    @Transactional
    public TicketDTO updateTicket(Long id, TicketUpdateDTO dto) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TicketMapper.updateEntity(dto, existing, company, customer, user);

        Ticket saved = ticketRepository.save(existing);

        return TicketMapper.toTicketDTO(saved);
    }

    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        ticketRepository.deleteById(id);
    }
}
