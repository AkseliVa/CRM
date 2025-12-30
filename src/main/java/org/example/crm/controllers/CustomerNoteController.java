package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.mappers.CustomerNoteMapper;
import org.example.crm.repositories.CustomerNotesRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-notes")
public class CustomerNoteController {
    private final CustomerNotesRepository customerNotesRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public CustomerNoteController(CustomerNotesRepository customerNotesRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.customerNotesRepository = customerNotesRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    };

    @GetMapping
    public List<CustomerNoteDTO> getCustomerNotes() {
        return customerNotesRepository.findAll()
                .stream()
                .map(CustomerNoteMapper::toCustomerNoteDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> getCustomerNotes(@PathVariable Long id) {
        return customerNotesRepository.findById(id)
                .map(c -> ResponseEntity.ok(CustomerNoteMapper.toCustomerNoteDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<CustomerNoteDTO> createCustomerNotes(@RequestBody CustomerNoteCreateDTO dto) {
        var customer = customerRepository.findById(dto.customerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.userId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        CustomerNotes notes = CustomerNoteMapper.fromCreateDTO(dto, customer, user);

        CustomerNotes saved = customerNotesRepository.save(notes);

        return ResponseEntity.ok(CustomerNoteMapper.toCustomerNoteDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> updateCustomerNotes(@PathVariable Long id, @RequestBody CustomerNoteUpdateDTO dto) {
        CustomerNotes existing= customerNotesRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        var customer = customerRepository.findById(dto.customerId()).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.userId()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        CustomerNoteMapper.updateEntity(existing, dto, customer, user);

        CustomerNotes saved = customerNotesRepository.save(existing);

        return ResponseEntity.ok(CustomerNoteMapper.toCustomerNoteDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerNotes(@PathVariable Long id) {

        if (!customerNotesRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        customerNotesRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
