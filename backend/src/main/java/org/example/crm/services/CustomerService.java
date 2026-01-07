package org.example.crm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.mappers.CustomerMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toCustomerDTO)
                .toList();
    }

    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toCustomerDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Customer customer = CustomerMapper.fromCreateDTO(dto, company);
        Customer saved = customerRepository.save(customer);

        return CustomerMapper.toCustomerDTO(saved);
    }

    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerUpdateDTO dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        CustomerMapper.updateEntity(existing, dto, company);

        Customer saved = customerRepository.save(existing);

        return CustomerMapper.toCustomerDTO(saved);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }

        customerRepository.deleteById(id);
    }
}
