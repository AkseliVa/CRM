package org.example.crm.controllers;

import org.example.crm.entities.Customer;
import org.example.crm.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.*;

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
        return customerRepository.save(customer);
    };
}
