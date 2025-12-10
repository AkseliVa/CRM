package org.example.crm.mappers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.entities.Ticket;
import org.example.crm.entities.User;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TicketMapperTest {

    @Test
    public void testFromCreateDTO() {
        Customer customer = new Customer();
        customer.setId(1L);

        Company company = new Company();
        company.setId(1L);

        User user = new User();
        user.setId(1L);

        TicketCreateDTO dto = new TicketCreateDTO("testtitle", "testdescription", TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS, company.getId(), customer.getId(), user.getId());

        Ticket ticket = TicketMapper.fromCreateDTO(dto, company, customer, user);

        assertNotNull(ticket);
        assertEquals("testtitle", ticket.getTitle());
        assertEquals("testdescription", ticket.getDescription());
        assertEquals(TicketPriority.MEDIUM, ticket.getPriority());
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        assertEquals(customer, ticket.getCustomer());
        assertEquals(company, ticket.getCompany());
        assertEquals(user, ticket.getAssignedUser());

        assertNotNull(ticket.getCreatedAt());
        assertNotNull(ticket.getUpdatedAt());
    }
}
