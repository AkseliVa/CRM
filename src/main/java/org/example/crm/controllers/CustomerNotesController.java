package org.example.crm.controllers;

import org.example.crm.entities.CustomerNotes;
import org.example.crm.repositories.CustomerNotesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer_notes")
public class CustomerNotesController {
    private final CustomerNotesRepository customerNotesRepository;

    public CustomerNotesController(CustomerNotesRepository customerNotesRepository) {
        this.customerNotesRepository = customerNotesRepository;
    };

    @GetMapping
    public List<CustomerNotes> getCustomerNotes() {
        return customerNotesRepository.findAll();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNotes> getCustomerNotes(@PathVariable Long id) {
        return customerNotesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public CustomerNotes createCustomerNotes(@RequestBody CustomerNotes customerNotes) {
        customerNotes.setCreated_at(LocalDateTime.now());
        customerNotes.setUpdated_at(LocalDateTime.now());
        return customerNotesRepository.save(customerNotes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNotes> updateCustomerNotes(@PathVariable Long id, @RequestBody CustomerNotes customerNotes) {
        CustomerNotes updatedCustomerNotes = customerNotesRepository.findById(id).orElse(null);

        if (updatedCustomerNotes == null) {
            return ResponseEntity.notFound().build();
        }

        updatedCustomerNotes.setContent(customerNotes.getContent());
        updatedCustomerNotes.setUpdated_at(LocalDateTime.now());

        return ResponseEntity.ok(customerNotesRepository.save(updatedCustomerNotes));
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
