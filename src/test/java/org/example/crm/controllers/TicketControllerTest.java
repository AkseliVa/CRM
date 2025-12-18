package org.example.crm.controllers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.entities.Ticket;
import org.example.crm.entities.User;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.TicketRepository;
import org.example.crm.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketRepository ticketRepository;

    @MockitoBean
    private CompanyRepository companyRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void createTicket_success() throws Exception {

        Company company = new Company();
        company.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        User user = new User();
        user.setId(3L);

        Ticket savedTicket = new Ticket();

        savedTicket.setId(10L);
        savedTicket.setCustomer(customer);
        savedTicket.setCompany(company);
        savedTicket.setAssignedUser(user);
        savedTicket.setTitle("Test ticket");
        savedTicket.setDescription("Test description");
        savedTicket.setStatus(TicketStatus.OPEN);
        savedTicket.setPriority(TicketPriority.LOW);
        savedTicket.setCreatedAt(LocalDateTime.now());
        savedTicket.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketCreateDTO dto = new TicketCreateDTO("Test ticket", "Test description", TicketPriority.LOW, TicketStatus.OPEN, 1L, 2L, 3L);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Test ticket"))
                .andExpect(jsonPath("$.companyId").value(1L))
                .andExpect(jsonPath("$.customerId").value(2L))
                .andExpect(jsonPath("$.assignedUserId").value(3L));

    }

    @Test
    void createTicket_failure_noCompany() throws Exception {

        Customer customer = new Customer();
        customer.setId(2L);

        User user = new User();
        user.setId(3L);

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        TicketCreateDTO dto = new TicketCreateDTO(
                "Test ticket",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.OPEN,
                1L,
                2L,
                3L
        );

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTicket_failure_noCustomer() throws Exception {

        Company company = new Company();
        company.setId(1L);

        User user = new User();
        user.setId(3L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        TicketCreateDTO dto = new TicketCreateDTO(
                "Test ticket",
                "Test description",
                TicketPriority.HIGH,
                TicketStatus.IN_PROGRESS,
                1L,
                2L,
                3L
        );

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTicket_failure_noUser() throws Exception {
        Company company = new Company();
        company.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        TicketCreateDTO dto = new TicketCreateDTO(
                "Test ticket",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                1L,
                2L,
                3L
        );

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
