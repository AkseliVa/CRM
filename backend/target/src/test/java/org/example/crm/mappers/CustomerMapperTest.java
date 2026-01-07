package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerMapperTest {

    @Test
    public void testFromCreateDTO() {
        Company company = new Company();
        company.setId(1L);

        CustomerCreateDTO dto = new CustomerCreateDTO("testcustomer", "testcustomeremail", "050223344", company.getId());

        Customer customer = CustomerMapper.fromCreateDTO(dto, company);

        assertNotNull(customer);
        assertEquals("testcustomer", customer.getName());
        assertEquals("testcustomeremail", customer.getEmail());
        assertEquals("050223344", customer.getPhone());
        assertEquals(company, customer.getCompany());

        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    public void testUpdateDTO() {
        Customer customer = new Customer();

        Company oldCompany = new Company();
        oldCompany.setId(99L);

        customer.setCompany(oldCompany);
        customer.setName("old name");
        customer.setEmail("old email");
        customer.setPhone("old phone");
        customer.setCreatedAt(LocalDateTime.now());

        Company newCompany = new Company();
        newCompany.setId(66L);

        CustomerUpdateDTO dto = new CustomerUpdateDTO("new name", "new email", "new phone", 99L);

        CustomerMapper.updateEntity(customer, dto, newCompany);

        assertEquals("new name", customer.getName());
        assertEquals("new email", customer.getEmail());
        assertEquals("new phone", customer.getPhone());
        assertEquals(newCompany, customer.getCompany());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }
}
