package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNotes;
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

        CustomerNotes customerNotes = CustomerNoteMapper.fromCreateDTO(dto, customer, user);

        assertNotNull(customerNotes);
        assertEquals(customer, customerNotes.getCustomer());
        assertEquals(user, customerNotes.getUser());
        assertEquals("test content", customerNotes.getContent());

        assertNotNull(customerNotes.getCreatedAt());
        assertNotNull(customerNotes.getUpdatedAt());
    }

    @Test
    public void testUpdateDTO() {
        CustomerNotes notes = new CustomerNotes();

        Customer oldCustomer = new Customer();
        oldCustomer.setId(55L);

        User oldUser = new User();
        oldUser.setId(69L);

        notes.setCustomer(oldCustomer);
        notes.setUser(oldUser);
        notes.setContent("Old content");
        notes.setCreatedAt(LocalDateTime.now());

        Customer newCustomer = new Customer();
        newCustomer.setId(54L);

        User newUser = new User();
        newUser.setId(96L);

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(54L, 96L, "New content");

        CustomerNoteMapper.updateEntity(notes, dto, newCustomer, newUser);

        assertEquals(newCustomer, notes.getCustomer());
        assertEquals(newUser, notes.getUser());
        assertEquals("New content", notes.getContent());
        assertNotNull(notes.getCreatedAt());
        assertNotNull(notes.getUpdatedAt());
    }
}
