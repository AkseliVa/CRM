package org.example.crm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.CustomerNote;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.mappers.CustomerNoteMapper;
import org.example.crm.repositories.CustomerNoteRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerNoteService {

    private final CustomerNoteRepository customerNoteRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public List<CustomerNoteDTO> getAllCustomerNotes() {
        return customerNoteRepository.findAllWithRelations().stream()
                .map(CustomerNoteMapper::toCustomerNoteDTO)
                .toList();
    }

    public CustomerNoteDTO getCustomerNoteById(Long id) {
        return customerNoteRepository.findById(id)
                .map(CustomerNoteMapper::toCustomerNoteDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customernote not found"));
    }

    @Transactional
    public CustomerNoteDTO createCustomerNote(CustomerNoteCreateDTO dto) {
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CustomerNote note = CustomerNoteMapper.fromCreateDTO(dto, customer, user);
        CustomerNote saved = customerNoteRepository.save(note);

        return CustomerNoteMapper.toCustomerNoteDTO(saved);
    }

    @Transactional
    public CustomerNoteDTO updateCustomerNote(Long id, CustomerNoteUpdateDTO dto) {
        CustomerNote existing = customerNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer note not found"));
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CustomerNoteMapper.updateEntity(existing, dto, customer, user);

        CustomerNote saved = customerNoteRepository.save(existing);

        return CustomerNoteMapper.toCustomerNoteDTO(saved);
    }

    @Transactional
    public void deleteCustomerNote(Long id) {
        if (!customerNoteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer note not found");
        }

        customerNoteRepository.deleteById(id);
    }
}
