package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.mappers.CustomerMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public CustomerController(CustomerRepository customerRepository, CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    };

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toCustomerDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(c -> ResponseEntity.ok(CustomerMapper.toCustomerDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                        .orElse(null);

        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        Customer customer = CustomerMapper.fromCreateDTO(dto, company);

        Customer saved = customerRepository.save(customer);

        return ResponseEntity.ok(CustomerMapper.toCustomerDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDTO dto) {

        Customer existing = customerRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        var company = companyRepository.findById(dto.companyId()).orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        CustomerMapper.updateEntity(existing, dto, company);

        Customer saved = customerRepository.save(existing);

        return ResponseEntity.ok(CustomerMapper.toCustomerDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        customerRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
