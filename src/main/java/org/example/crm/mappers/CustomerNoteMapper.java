package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class CustomerNoteMapper {

    public static CustomerNoteDTO toCustomerNoteDTO(CustomerNotes customerNotes) {
        return new CustomerNoteDTO(
                customerNotes.getId(),
                customerNotes.getCustomer().getId(),
                customerNotes.getUser().getId(),
                customerNotes.getContent(),
                customerNotes.getCreatedAt(),
                customerNotes.getUpdatedAt()
        );
    }

    public static CustomerNotes fromCreateDTO(CustomerNoteCreateDTO dto, Customer customer, User user) {
        CustomerNotes notes = new CustomerNotes();

        notes.setCustomer(customer);
        notes.setUser(user);
        notes.setContent(dto.content());
        notes.setCreatedAt(LocalDateTime.now());
        notes.setUpdatedAt(LocalDateTime.now());

        return notes;
    }

    public static void updateEntity(CustomerNotes notes, CustomerNoteUpdateDTO dto, Customer customer, User user) {
        notes.setCustomer(customer);
        notes.setUser(user);
        notes.setContent(dto.content());
        notes.setUpdatedAt(LocalDateTime.now());
    }
}
