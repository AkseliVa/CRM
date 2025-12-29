package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private CompanyRepository companyRepository;

    @Test
    void getCustomers() throws Exception {

        Company company = new Company();

        company.setId(10L);

        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("Testname");
        customer.setEmail("test@email.com");
        customer.setPhone("0401234");
        customer.setCompany(company);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Testname"))
                .andExpect(jsonPath("[0].email").value("test@email.com"))
                .andExpect(jsonPath("$[0].phone").value("0401234"));
    }

    @Test
    void getCustomer_success() throws Exception {

        Company company = new Company();

        company.setId(10L);

        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("Testname");
        customer.setEmail("test@email.com");
        customer.setPhone("0501324");
        customer.setCompany(company);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Testname"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("0501324"));
    }

    @Test
    void getCustomer_failure() throws Exception {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_success() throws Exception {

        Company company = new Company();
        company.setId(1L);

        Customer customer = new Customer();

        customer.setId(10L);
        customer.setName("Test customer");
        customer.setPhone("040111222");
        customer.setEmail("test@email.com");
        customer.setCompany(company);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerCreateDTO dto = new CustomerCreateDTO(
                "Test customer",
                "test@email.com",
                "040111222",
                1L
        );

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Test customer"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("040111222"))
                .andExpect(jsonPath("$.companyId").value(1L));
    }

    @Test
    void createCustomer_failure_noCompany() throws Exception {

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerCreateDTO dto = new CustomerCreateDTO(
                "Test customer",
                "test@email.com",
                "040111222",
                1L
        );

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer_success() throws Exception {

        Company oldCompany = new Company();
        oldCompany.setId(1L);
        Company newCompany = new Company();
        newCompany.setId(11L);

        Customer customer = new Customer();

        customer.setId(10L);
        customer.setName("Test customer");
        customer.setEmail("test@email.com");
        customer.setPhone("040111222");
        customer.setCompany(oldCompany);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(companyRepository.findById(11L)).thenReturn(Optional.of(newCompany));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerUpdateDTO dto = new CustomerUpdateDTO(
                "Test customer updated",
                "updated@email.com",
                "050222111",
                11L
        );

        mockMvc.perform(put("/api/customers/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Test customer updated"))
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.phone").value("050222111"))
                .andExpect(jsonPath("$.companyId").value(11L));
    }

    @Test
    void updateCustomer_failure_noCustomer() throws Exception {

        when(customerRepository.findById(10L)).thenReturn(Optional.empty());

        CustomerUpdateDTO dto = new CustomerUpdateDTO(
                "Test customer",
                "test@email.com",
                "040111222",
                11L
        );

        mockMvc.perform(put("/api/customers/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTicket_failure_noCompany() throws Exception {

        Company oldCompany = new Company();
        oldCompany.setId(1L);

        Customer customer = new Customer();

        customer.setId(10L);
        customer.setName("Test customer");
        customer.setEmail("test@email.com");
        customer.setPhone("040111222");
        customer.setCompany(oldCompany);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(companyRepository.findById(11L)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerUpdateDTO dto = new CustomerUpdateDTO(
                "updated name",
                "updated email",
                "050222111",
                11L
        );

        mockMvc.perform(put("/api/customers/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomer_success() throws Exception {

        when(customerRepository.existsById(10L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(customerRepository).deleteById(10L);
    }

    @Test
    void deleteCustomer_failure_noCustomer() throws Exception {

        when(customerRepository.existsById(10L)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/{id}", 10L))
                .andExpect(status().isNotFound());

        verify(customerRepository, never()).deleteById(any());
    }
}
