package org.example.crm.controllers;

import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.CustomerNote;
import org.example.crm.mappers.CustomerNoteMapper;
import org.example.crm.repositories.CustomerNoteRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.UserRepository;
import org.example.crm.services.CustomerNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-notes")
@RequiredArgsConstructor
public class CustomerNoteController {

    private final CustomerNoteService customerNoteService;

    @GetMapping
    public List<CustomerNoteDTO> getCustomerNotes() {
        return customerNoteService.getAllCustomerNotes();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> getCustomerNotes(@PathVariable Long id) {
        return ResponseEntity.ok(customerNoteService.getCustomerNoteById(id));
    };

    @PostMapping
    public ResponseEntity<CustomerNoteDTO> createCustomerNotes(@RequestBody CustomerNoteCreateDTO dto) {
        CustomerNoteDTO created = customerNoteService.createCustomerNote(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> updateCustomerNotes(@PathVariable Long id, @RequestBody CustomerNoteUpdateDTO dto) {
        return ResponseEntity.ok(customerNoteService.updateCustomerNote(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerNotes(@PathVariable Long id) {
        customerNoteService.deleteCustomerNote(id);
        return ResponseEntity.noContent().build();
    }
}
