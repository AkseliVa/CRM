package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerNotesCreateDTO;
import org.example.crm.DTOs.CustomerNotesDTO;
import org.example.crm.DTOs.CustomerNotesUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class CustomerNotesMapper {

    public static CustomerNotesDTO toCustomerNotesDTO(CustomerNotes customerNotes) {
        return new CustomerNotesDTO(
                customerNotes.getId(),
                customerNotes.getCustomer().getId(),
                customerNotes.getUser().getId(),
                customerNotes.getContent(),
                customerNotes.getCreatedAt(),
                customerNotes.getUpdatedAt()
        );
    }

    public static CustomerNotes fromCreateDTO(CustomerNotesCreateDTO dto, Customer customer, User user) {
        CustomerNotes notes = new CustomerNotes();

        notes.setCustomer(customer);
        notes.setUser(user);
        notes.setContent(dto.content());
        notes.setCreatedAt(LocalDateTime.now());
        notes.setUpdatedAt(LocalDateTime.now());

        return notes;
    }

    public static void updateEntity(CustomerNotes notes, CustomerNotesUpdateDTO dto, Customer customer, User user) {
        notes.setCustomer(customer);
        notes.setUser(user);
        notes.setContent(dto.content());
        notes.setUpdatedAt(LocalDateTime.now());
    }
}
