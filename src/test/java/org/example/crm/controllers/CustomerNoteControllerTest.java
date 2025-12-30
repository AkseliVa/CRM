package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.entities.Customer;
import org.example.crm.entities.CustomerNote;
import org.example.crm.entities.User;
import org.example.crm.repositories.CustomerNoteRepository;
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

@WebMvcTest(CustomerNoteController.class)
public class CustomerNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerNoteRepository customerNoteRepository;

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

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setContent("Test content");
        customerNote.setCustomer(customer);
        customerNote.setUser(user);
        customerNote.setCreatedAt(LocalDateTime.now());
        customerNote.setUpdatedAt(LocalDateTime.now());

        when(customerNoteRepository.findAll()).thenReturn(List.of(customerNote));

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

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setContent("Test content");
        customerNote.setCustomer(customer);
        customerNote.setUser(user);
        customerNote.setCreatedAt(LocalDateTime.now());
        customerNote.setUpdatedAt(LocalDateTime.now());

        when(customerNoteRepository.findById(1L)).thenReturn(Optional.of(customerNote));

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(11L));
    }

    @Test
    void getCustomerNote_failure() throws Exception {

        when(customerNoteRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomerNotes() throws Exception {

        Customer customer = new Customer();
        customer.setId(10L);

        User user = new User();
        user.setId(11L);

        CustomerNote savedCustomerNote = new CustomerNote();
        savedCustomerNote.setId(1L);
        savedCustomerNote.setContent("Test content");
        savedCustomerNote.setCustomer(customer);
        savedCustomerNote.setUser(user);
        savedCustomerNote.setCreatedAt(LocalDateTime.now());
        savedCustomerNote.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(11L)).thenReturn(Optional.of(user));
        when(customerNoteRepository.save(any(CustomerNote.class))).thenReturn(savedCustomerNote);

        CustomerNoteCreateDTO dto = new CustomerNoteCreateDTO(10L, 11L, "Test content");

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

        CustomerNoteCreateDTO dto = new CustomerNoteCreateDTO(10L, 11L, "Test content");

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

        CustomerNoteCreateDTO dto = new CustomerNoteCreateDTO(10L, 11L, "Test content");

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

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(10L);
        customerNote.setContent("Test content");
        customerNote.setCustomer(oldCustomer);
        customerNote.setUser(oldUser);
        customerNote.setCreatedAt(LocalDateTime.now());
        customerNote.setUpdatedAt(LocalDateTime.now());

        when(customerNoteRepository.findById(10L)).thenReturn(Optional.of(customerNote));
        when(customerRepository.findById(11L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(22L)).thenReturn(Optional.of(newUser));
        when(customerNoteRepository.save(any(CustomerNote.class))).thenReturn(customerNote);

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(11L, 22L, "Updated content");

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

        when(customerNoteRepository.findById(10L)).thenReturn(Optional.empty());

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(
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

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(10L);
        customerNote.setContent("Test content");
        customerNote.setCustomer(oldCustomer);
        customerNote.setUser(oldUser);
        customerNote.setCreatedAt(LocalDateTime.now());
        customerNote.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(11L)).thenReturn(Optional.empty());
        when(userRepository.findById(22L)).thenReturn(Optional.of(newUser));
        when(customerNoteRepository.save(any(CustomerNote.class))).thenReturn(customerNote);

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(
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

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(10L);
        customerNote.setContent("Test content");
        customerNote.setCustomer(oldCustomer);
        customerNote.setUser(oldUser);
        customerNote.setCreatedAt(LocalDateTime.now());
        customerNote.setUpdatedAt(LocalDateTime.now());

        when(customerRepository.findById(11L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(22L)).thenReturn(Optional.empty());
        when(customerNoteRepository.save(any(CustomerNote.class))).thenReturn(customerNote);

        CustomerNoteUpdateDTO dto = new CustomerNoteUpdateDTO(
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

        when(customerNoteRepository.existsById(10L)).thenReturn(true);

        mockMvc.perform(delete("/api/customer-notes/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(customerNoteRepository).deleteById(10L);
    }

    @Test
    void deleteCustomerNotes_failure() throws Exception {

        when(customerNoteRepository.existsById(10L)).thenReturn(false);

        mockMvc.perform(delete("/api/customer-notes/{id}", 10L))
                .andExpect(status().isNotFound());

        verify(customerNoteRepository, never()).deleteById(any());
    }
}
