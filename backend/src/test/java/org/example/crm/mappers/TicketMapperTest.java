package org.example.crm.mappers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.entities.Ticket;
import org.example.crm.entities.User;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

    @Test
    public void testUpdateDTO() {
        Ticket ticket = new Ticket();

        Company oldCompany = new Company();
        oldCompany.setId(1L);

        Customer oldCustomer = new Customer();
        oldCustomer.setId(2L);

        User oldUser = new User();
        oldUser.setId(3L);

        ticket.setCompany(oldCompany);
        ticket.setCustomer(oldCustomer);
        ticket.setAssignedUser(oldUser);
        ticket.setTitle("old title");
        ticket.setDescription("old description");
        ticket.setPriority(TicketPriority.LOW);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        Company newCompany = new Company();
        newCompany.setId(99L);

        Customer newCustomer = new Customer();
        newCustomer.setId(98L);

        User newUser = new User();
        newUser.setId(97L);

        TicketUpdateDTO dto = new TicketUpdateDTO("new title", "new description", TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS, 99L, 98L, 97L);

        TicketMapper.updateEntity(dto, ticket, newCompany, newCustomer, newUser);

        assertEquals("new title", ticket.getTitle());
        assertEquals("new description", ticket.getDescription());
        assertEquals(TicketPriority.MEDIUM, ticket.getPriority());
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        assertEquals(newCompany, ticket.getCompany());
        assertEquals(newCustomer, ticket.getCustomer());
        assertEquals(newUser, ticket.getAssignedUser());
        assertNotNull(ticket.getCreatedAt());
        assertNotNull(ticket.getUpdatedAt());
    }
}
