package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerNotesCreateDTO;
import org.example.crm.DTOs.CustomerNotesDTO;
import org.example.crm.DTOs.CustomerNotesUpdateDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.mappers.CustomerMapper;
import org.example.crm.mappers.CustomerNotesMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerNotesRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer_notes")
public class CustomerNotesController {
    private final CustomerNotesRepository customerNotesRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public CustomerNotesController(CustomerNotesRepository customerNotesRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.customerNotesRepository = customerNotesRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    };

    @GetMapping
    public List<CustomerNotesDTO> getCustomerNotes() {
        return customerNotesRepository.findAll()
                .stream()
                .map(CustomerNotesMapper::toCustomerNotesDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNotesDTO> getCustomerNotes(@PathVariable Long id) {
        return customerNotesRepository.findById(id)
                .map(c -> ResponseEntity.ok(CustomerNotesMapper.toCustomerNotesDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<CustomerNotesDTO> createCustomerNotes(@RequestBody CustomerNotesCreateDTO dto) {
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

        CustomerNotes notes = CustomerNotesMapper.fromCreateDTO(dto, customer, user);

        CustomerNotes saved = customerNotesRepository.save(notes);

        return ResponseEntity.ok(CustomerNotesMapper.toCustomerNotesDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNotesDTO> updateCustomerNotes(@PathVariable Long id, @RequestBody CustomerNotesUpdateDTO dto) {
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

        CustomerNotesMapper.updateEntity(existing, dto, customer, user);

        CustomerNotes saved = customerNotesRepository.save(existing);

        return ResponseEntity.ok(CustomerNotesMapper.toCustomerNotesDTO(saved));
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
