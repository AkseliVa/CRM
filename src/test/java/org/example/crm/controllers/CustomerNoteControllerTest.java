package org.example.crm.controllers;

import org.example.crm.DTOs.CustomerNoteCreateDTO;
import org.example.crm.DTOs.CustomerNoteDTO;
import org.example.crm.DTOs.CustomerNoteUpdateDTO;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.services.CustomerNoteService;
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

@WebMvcTest(CustomerNoteController.class)
public class CustomerNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerNoteService customerNoteService;

    @Test
    void getCustomerNotes_success() throws Exception {
        CustomerNoteDTO dto = new CustomerNoteDTO(
                1L,
                10L,
                20L,
                "Test content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerNoteService.getAllCustomerNotes()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/customer-notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"))
                .andExpect(jsonPath("$[0].customerId").value(10L))
                .andExpect(jsonPath("$[0].userId").value(20L));
    }

    @Test
    void getCustomerNote_success() throws Exception {
        CustomerNoteDTO dto = new CustomerNoteDTO(
                1L,
                10L,
                20L,
                "Test content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerNoteService.getCustomerNoteById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(20L));
    }

    @Test
    void getCustomerNote_failure() throws Exception {
        when(customerNoteService.getCustomerNoteById(1L))
                .thenThrow(new ResourceNotFoundException("Customer note not found"));

        mockMvc.perform(get("/api/customer-notes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomerNote_success() throws Exception {
        CustomerNoteCreateDTO createDto = new CustomerNoteCreateDTO(
                10L,
                20L,
                "Test content"
        );
        CustomerNoteDTO resultDto = new CustomerNoteDTO(
                1L,
                10L,
                20L,
                "Test content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerNoteService.createCustomerNote(any(CustomerNoteCreateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/customer-notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(20L));
    }

    @Test
    void updateCustomerNote_success() throws Exception {
        CustomerNoteUpdateDTO updateDto = new CustomerNoteUpdateDTO(
                10L,
                20L,
                "Test content"
        );
        CustomerNoteDTO resultDto = new CustomerNoteDTO(
                1L,
                10L,
                20L,
                "Test content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerNoteService.updateCustomerNote(eq(1L), any(CustomerNoteUpdateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/customer-notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.customerId").value(10L))
                .andExpect(jsonPath("$.userId").value(20L));
    }

    @Test
    void deleteCustomerNote_success() throws Exception {
        doNothing().when(customerNoteService).deleteCustomerNote(1L);

        mockMvc.perform(delete("/api/customer-notes/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(customerNoteService, times(1)).deleteCustomerNote(1L);
    }

    @Test
    void deleteCustomerNotes_failure() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(customerNoteService).deleteCustomerNote(1L);

        mockMvc.perform(delete("/api/customer-notes/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
