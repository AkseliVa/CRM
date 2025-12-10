package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.junit.jupiter.api.Test;

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
}
