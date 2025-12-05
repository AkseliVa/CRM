package org.example.crm.mappers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;

import java.time.LocalDateTime;

public class CustomerMapper {

    public static CustomerDTO toCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getCompany().getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    public static Customer fromCreateDTO(CustomerCreateDTO dto, Company company) {
        Customer customer = new Customer();

        customer.setName(dto.name());
        customer.setCompany(company);
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        return customer;
    }

    public static void updateEntity(Customer customer, CustomerUpdateDTO dto, Company company) {
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setCompany(company);
    }
}
