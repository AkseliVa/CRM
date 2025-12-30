package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNote;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class CustomerNoteMapper {

    public static CustomerNoteDTO toCustomerNoteDTO(CustomerNote customerNote) {
        return new CustomerNoteDTO(
                customerNote.getId(),
                customerNote.getCustomer().getId(),
                customerNote.getUser().getId(),
                customerNote.getContent(),
                customerNote.getCreatedAt(),
                customerNote.getUpdatedAt()
        );
    }

    public static CustomerNote fromCreateDTO(CustomerNoteCreateDTO dto, Customer customer, User user) {
        CustomerNote note = new CustomerNote();

        note.setCustomer(customer);
        note.setUser(user);
        note.setContent(dto.content());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());

        return note;
    }

    public static void updateEntity(CustomerNote note, CustomerNoteUpdateDTO dto, Customer customer, User user) {
        note.setCustomer(customer);
        note.setUser(user);
        note.setContent(dto.content());
        note.setUpdatedAt(LocalDateTime.now());
    }
}
