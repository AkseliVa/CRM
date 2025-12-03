package org.example.crm.controllers;

import org.example.crm.entities.Customer;
import org.example.crm.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    };

    @GetMapping("/api/customer")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    };

    @GetMapping("/api/customer/{id}")
    public Optional<Customer> getCustomer(@PathVariable Long id) {
        return customerRepository.findById(id);
    };

    @PostMapping("/api/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        customer.setCreated_at(LocalDateTime.now());
        customer.setUpdated_at(LocalDateTime.now());
        return customerRepository.save(customer);
    };

    @PutMapping("/api/customer/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerRepository.findById(id).orElse(null);

        updatedCustomer.setCompany(customer.getCompany());
        updatedCustomer.setName(customer.getName());
        updatedCustomer.setEmail(customer.getEmail());
        updatedCustomer.setPhone(customer.getPhone());
        updatedCustomer.setUpdated_at(LocalDateTime.now());

        return customerRepository.save(updatedCustomer);
    }
}
