package org.example.crm.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
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
    public List<CustomerNoteDTO> getCustomerNote() {
        return customerNoteService.getAllCustomerNotes();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> getCustomerNote(@PathVariable Long id) {
        return ResponseEntity.ok(customerNoteService.getCustomerNoteById(id));
    };

    @PostMapping
    public ResponseEntity<CustomerNoteDTO> createCustomerNote(@Valid @RequestBody CustomerNoteCreateDTO dto) {
        CustomerNoteDTO created = customerNoteService.createCustomerNote(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNoteDTO> updateCustomerNote(@PathVariable Long id, @Valid @RequestBody CustomerNoteUpdateDTO dto) {
        return ResponseEntity.ok(customerNoteService.updateCustomerNote(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerNote(@PathVariable Long id) {
        customerNoteService.deleteCustomerNote(id);
        return ResponseEntity.noContent().build();
    }
}
