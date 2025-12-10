package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerNotesCreateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerNotesMapperTest {

    @Test
    public void testFromCreateDTO() {
        Customer customer = new Customer();
        customer.setId(1L);

        User user = new User();
        user.setId(1L);


        CustomerNotesCreateDTO dto = new CustomerNotesCreateDTO(customer.getId(), user.getId(), "test content");

        CustomerNotes customerNotes = CustomerNotesMapper.fromCreateDTO(dto, customer, user);

        assertNotNull(customerNotes);
        assertEquals(customer, customerNotes.getCustomer());
        assertEquals(user, customerNotes.getUser());
        assertEquals("test content", customerNotes.getContent());

        assertNotNull(customerNotes.getCreatedAt());
        assertNotNull(customerNotes.getUpdatedAt());
    }
}
