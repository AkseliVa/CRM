package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerCreateDTO;
import org.example.crm.DTOs.CustomerDTO;
import org.example.crm.DTOs.CustomerUpdateDTO;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

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
    private CustomerService customerService;

    @Test
    void getCustomers_success() throws Exception {
        CustomerDTO dto = new CustomerDTO(
                1L,
                10L,
                "Testname",
                "test@email.com",
                "0401234",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.getAllCustomers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Testname"))
                .andExpect(jsonPath("[0].email").value("test@email.com"))
                .andExpect(jsonPath("$[0].phone").value("0401234"));
    }

    @Test
    void getCustomer_success() throws Exception {
        CustomerDTO dto = new CustomerDTO(
                1L,
                10L,
                "Testname",
                "test@email.com",
                "0401234",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.getCustomerById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Testname"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("0401234"));
    }

    @Test
    void getCustomer_failure() throws Exception {
        when(customerService.getCustomerById(1L))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_success() throws Exception {
        CustomerCreateDTO createDto = new CustomerCreateDTO(
                "Testname",
                "test@email.com",
                "0401234",
                10L
        );
        CustomerDTO resultDto = new CustomerDTO(
                1L,
                10L,
                "Testname",
                "test@email.com",
                "0401234",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any(CustomerCreateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Testname"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("0401234"))
                .andExpect(jsonPath("$.companyId").value(10L));
    }

    @Test
    void updateCustomer_success() throws Exception {
        CustomerUpdateDTO updateDto = new CustomerUpdateDTO(
                "Testname",
                "test@email.com",
                "0401234",
                10L
        );
        CustomerDTO resultDto = new CustomerDTO(
                1L,
                10L,
                "Testname",
                "test@email.com",
                "0401234",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.updateCustomer(eq(1L), any(CustomerUpdateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/customers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Testname"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("0401234"))
                .andExpect(jsonPath("$.companyId").value(10L));
    }

    @Test
    void deleteCustomer_success() throws Exception {
        doNothing().when(customerService).deleteCustomer(10L);

        mockMvc.perform(delete("/api/customers/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(10L);
    }

    @Test
    void deleteCustomer_failure() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(customerService).deleteCustomer(10L);

        mockMvc.perform(delete("/api/customers/{id}", 10L))
                .andExpect(status().isNotFound());
    }
}
