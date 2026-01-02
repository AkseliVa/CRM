package org.example.crm.controllers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.services.TicketService;
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

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService ticketService;

    @Test
    void getTickets_success() throws Exception {
        TicketDTO dto = new TicketDTO(
                1L,
                "Test Ticket",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(ticketService.getAllTickets()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Ticket"))
                .andExpect(jsonPath("$[0].description").value("Test description"));
    }

    @Test
    void getTicket_success() throws Exception {
        TicketDTO dto = new TicketDTO(
                1L,
                "Test Title",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(ticketService.getTicketById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/tickets/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.companyId").value(10L))
                .andExpect(jsonPath("$.customerId").value(20L))
                .andExpect(jsonPath("$.assignedUserId").value(30L));
    }

    @Test
    void getTicket_failure() throws Exception {
        when(ticketService.getTicketById(1L))
                .thenThrow(new ResourceNotFoundException("Ticket not found"));

        mockMvc.perform(get("/api/tickets/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTicket_success() throws Exception {
        TicketCreateDTO createDto = new TicketCreateDTO(
                "Test Title",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L
        );
        TicketDTO resultDto = new TicketDTO(
                1L,
                "Test Title",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(ticketService.createTicket(any(TicketCreateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.companyId").value(10L))
                .andExpect(jsonPath("$.customerId").value(20L))
                .andExpect(jsonPath("$.assignedUserId").value(30L));
    }

    @Test
    void updateTicket_success() throws Exception {
        TicketUpdateDTO updateDto = new TicketUpdateDTO(
                "Test title",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L
        );
        TicketDTO resultDto = new TicketDTO(
                1L,
                "Test Title",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                10L,
                20L,
                30L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(ticketService.updateTicket(eq(1L), any(TicketUpdateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/tickets/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.companyId").value(10L))
                .andExpect(jsonPath("$.customerId").value(20L))
                .andExpect(jsonPath("$.assignedUserId").value(30L));
    }

    @Test
    void deleteTicket_success() throws Exception {
        doNothing().when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/api/tickets/{id}", 1L))
                        .andExpect(status().isNoContent());

        verify(ticketService, times(1)).deleteTicket(1L);
    }

    @Test
    void deleteTicket_failure_noTicket() throws Exception {
        doThrow(new ResourceNotFoundException("Ticket not found")).when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/api/tickets/{id}", 1L));
    }
}
