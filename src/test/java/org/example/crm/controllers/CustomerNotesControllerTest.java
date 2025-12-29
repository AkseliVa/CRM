package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerNotesCreateDTO;
import org.example.crm.DTOs.CustomerNotesUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNotes;
import org.example.crm.entities.User;
import org.example.crm.repositories.CustomerNotesRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.UserRepository;
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

@WebMvcTest(CustomerNotesController.class)
public class CustomerNotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerNotesRepository customerNotesRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void getCustomerNotes() throws Exception {

        Customer customer = new Customer();
        customer.setId(10L);

        User user = new User();
        user.setId(11L);

        CustomerNotes customerNotes = new CustomerNotes();
        customerNotes.setId(1L);
        customerNotes.setContent("Test content");
        customerNotes.setCustomer(customer);
        customerNotes.setUser(user);
        customerNotes.setCreatedAt(LocalDateTime.now());
        customerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerNotesRepository.findAll()).thenReturn(List.of(customerNotes));

        mockMvc.perform(get("/api/customer-notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"))
                .andExpect(jsonPath("$[0].customerId").value(10L))
                .andExpect(jsonPath("$[0].userId").value(11L));
    }

    @Test
    void getCustomerNote_success() throws Exception {

        Customer customer = new Customer();
        customer.setId(10L);

        User user = new User();
        user.setId(11L);

        CustomerNotes customerNotes = new CustomerNotes();
        customerNotes.setId(1L);
        customerNotes.setContent("Test content");
        customerNotes.setCustomer(customer);
        customerNotes.setUser(user);
        customerNotes.setCreatedAt(LocalDateTime.now());
        customerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerNotesRepository.findById(1L)).thenReturn(Optional.of(customerNotes));

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(11L));
    }

    @Test
    void getCustomerNote_failure() throws Exception {

        when(customerNotesRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomerNotes() throws Exception {

        Customer customer = new Customer();
        customer.setId(10L);

        User user = new User();
        user.setId(11L);

        CustomerNotes savedCustomerNotes = new CustomerNotes();
        savedCustomerNotes.setId(1L);
        savedCustomerNotes.setContent("Test content");
        savedCustomerNotes.setCustomer(customer);
        savedCustomerNotes.setUser(user);
        savedCustomerNotes.setCreatedAt(LocalDateTime.now());
        savedCustomerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(11L)).thenReturn(Optional.of(user));
        when(customerNotesRepository.save(any(CustomerNotes.class))).thenReturn(savedCustomerNotes);

        CustomerNotesCreateDTO dto = new CustomerNotesCreateDTO(10L, 11L, "Test content");

        mockMvc.perform(post("/api/customer-notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(11L));
    }

    @Test
    void createCustomerNotes_failure_noCustomer() throws Exception {

        User user = new User();
        user.setId(11L);

        when(customerRepository.findById(10L)).thenReturn(Optional.empty());
        when(userRepository.findById(11L)).thenReturn(Optional.of(user));

        CustomerNotesCreateDTO dto = new CustomerNotesCreateDTO(10L, 11L, "Test content");

        mockMvc.perform(post("/api/customer-notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomerNotes_failure_noUser() throws Exception {

        Customer customer = new Customer();
        customer.setId(10L);

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(11L)).thenReturn(Optional.empty());

        CustomerNotesCreateDTO dto = new CustomerNotesCreateDTO(10L, 11L, "Test content");

        mockMvc.perform(post("/api/customer-notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomerNotes_success() throws Exception {

        Customer oldCustomer = new Customer();
        oldCustomer.setId(1L);
        Customer newCustomer = new Customer();
        newCustomer.setId(11L);

        User oldUser = new User();
        oldUser.setId(2L);
        User newUser = new User();
        newUser.setId(22L);

        CustomerNotes customerNotes = new CustomerNotes();
        customerNotes.setId(10L);
        customerNotes.setContent("Test content");
        customerNotes.setCustomer(oldCustomer);
        customerNotes.setUser(oldUser);
        customerNotes.setCreatedAt(LocalDateTime.now());
        customerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerNotesRepository.findById(10L)).thenReturn(Optional.of(customerNotes));
        when(customerRepository.findById(11L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(22L)).thenReturn(Optional.of(newUser));
        when(customerNotesRepository.save(any(CustomerNotes.class))).thenReturn(customerNotes);

        CustomerNotesUpdateDTO dto = new CustomerNotesUpdateDTO(11L, 22L, "Updated content");

        mockMvc.perform(put("/api/customer-notes/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.content").value("Updated content"))
                .andExpect(jsonPath("$.customerId").value(11L))
                .andExpect(jsonPath("$.userId").value(22L));
    }

    @Test
    void updateCustomerNotes_failure_noCustomerNotes() throws Exception {

        when(customerNotesRepository.findById(10L)).thenReturn(Optional.empty());

        CustomerNotesUpdateDTO dto = new CustomerNotesUpdateDTO(
                1L,
                2L,
                "Test content"
        );

        mockMvc.perform(put("/api/customer-notes/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomerNotes_failure_noCustomer() throws Exception {

        Customer oldCustomer = new Customer();
        oldCustomer.setId(1L);

        User oldUser = new User();
        oldUser.setId(2L);
        User newUser = new User();
        newUser.setId(22L);

        CustomerNotes customerNotes = new CustomerNotes();
        customerNotes.setId(10L);
        customerNotes.setContent("Test content");
        customerNotes.setCustomer(oldCustomer);
        customerNotes.setUser(oldUser);
        customerNotes.setCreatedAt(LocalDateTime.now());
        customerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(11L)).thenReturn(Optional.empty());
        when(userRepository.findById(22L)).thenReturn(Optional.of(newUser));
        when(customerNotesRepository.save(any(CustomerNotes.class))).thenReturn(customerNotes);

        CustomerNotesUpdateDTO dto = new CustomerNotesUpdateDTO(
                11L,
                22L,
                "Updated content"
        );

        mockMvc.perform(put("/api/customer-notes/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomerNotes_failure_noUser() throws Exception {

        Customer oldCustomer = new Customer();
        oldCustomer.setId(1L);
        Customer newCustomer = new Customer();
        newCustomer.setId(11L);

        User oldUser = new User();
        oldUser.setId(2L);

        CustomerNotes customerNotes = new CustomerNotes();
        customerNotes.setId(10L);
        customerNotes.setContent("Test content");
        customerNotes.setCustomer(oldCustomer);
        customerNotes.setUser(oldUser);
        customerNotes.setCreatedAt(LocalDateTime.now());
        customerNotes.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(11L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(22L)).thenReturn(Optional.empty());
        when(customerNotesRepository.save(any(CustomerNotes.class))).thenReturn(customerNotes);

        CustomerNotesUpdateDTO dto = new CustomerNotesUpdateDTO(
                11L,
                22L,
                "Updated content"
        );

        mockMvc.perform(put("/api/customer-notes/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomerNotes_success() throws Exception {

        when(customerNotesRepository.existsById(10L)).thenReturn(true);

        mockMvc.perform(delete("/api/customer-notes/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(customerNotesRepository).deleteById(10L);
    }

    @Test
    void deleteCustomerNotes_failure() throws Exception {

        when(customerNotesRepository.existsById(10L)).thenReturn(false);

        mockMvc.perform(delete("/api/customer-notes/{id}", 10L))
                .andExpect(status().isNotFound());

        verify(customerNotesRepository, never()).deleteById(any());
    }
}
