package org.example.crm.controllers;

import org.example.crm.entities.CustomerNotes;
import org.example.crm.repositories.CustomerNotesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerNotesController {
    private final CustomerNotesRepository customerNotesRepository;

    public CustomerNotesController(CustomerNotesRepository customerNotesRepository) {
        this.customerNotesRepository = customerNotesRepository;
    };

    @GetMapping("/api/customer_notes")
    public List<CustomerNotes> getCustomerNotes() {
        return customerNotesRepository.findAll();
    };

    @GetMapping("/api/customer_notes/{id}")
    public Optional<CustomerNotes> getCustomerNotes(@PathVariable Long id) {
        return customerNotesRepository.findById(id);
    };

    @PostMapping("/api/customer_notes")
    public CustomerNotes createCustomerNotes(@RequestBody CustomerNotes customerNotes) {
        return customerNotesRepository.save(customerNotes);
    }
}
