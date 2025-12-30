package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNote;
import org.example.crm.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerNoteMapperTest {

    @Test
    public void testFromCreateDTO() {
        Customer customer = new Customer();
        customer.setId(1L);

        User user = new User();
        user.setId(1L);


        CustomerNoteCreateDTO dto = new CustomerNoteCreateDTO(customer.getId(), user.getId(), "test content");

        CustomerNote customerNote = CustomerNoteMapper.fromCreateDTO(dto, customer, user);

        assertNotNull(customerNote);
        assertEquals(customer, customerNote.getCustomer());
        assertEquals(user, customerNote.getUser());
        assertEquals("test content", customerNote.getContent());

        assertNotNull(customerNote.getCreatedAt());
        assertNotNull(customerNote.getUpdatedAt());
    }

    @Test
    public void testUpdateDTO() {
        CustomerNote note = new CustomerNote();

        Customer oldCustomer = new Customer();
        oldCustomer.setId(55L);

        User oldUser = new User();
        oldUser.setId(69L);

        note.setCustomer(oldCustomer);
        note.setUser(oldUser);
        note.setContent("Old content");
        note.setCreatedAt(LocalDateTime.now());

        Customer newCustomer = new Customer();
        newCustomer.setId(54L);

        User newUser = new User();
        newUser.setId(96L);

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(54L, 96L, "New content");

        CustomerNoteMapper.updateEntity(note, dto, newCustomer, newUser);

        assertEquals(newCustomer, note.getCustomer());
        assertEquals(newUser, note.getUser());
        assertEquals("New content", note.getContent());
        assertNotNull(note.getCreatedAt());
        assertNotNull(note.getUpdatedAt());
    }
}
