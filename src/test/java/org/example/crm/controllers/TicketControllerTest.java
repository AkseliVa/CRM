package org.example.crm.controllers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
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
import java.util.List;
import java.util.Optional;

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
    private TicketRepository ticketRepository;

    @MockitoBean
    private CompanyRepository companyRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void getTickets() throws Exception {

        Company company = new Company();
        company.setId(10L);

        Customer customer = new Customer();
        customer.setId(11L);

        User user = new User();
        user.setId(12L);

        Ticket ticket = new Ticket();

        ticket.setId(1L);
        ticket.setTitle("Test Title");
        ticket.setDescription("Test Description");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.LOW);
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    @Test
    void getTicket_success() throws Exception {

        Company company = new Company();
        company.setId(10L);

        Customer customer = new Customer();
        customer.setId(11L);

        User user = new User();
        user.setId(12L);

        Ticket ticket = new Ticket();

        ticket.setId(1L);
        ticket.setTitle("Test Title");
        ticket.setDescription("Test Description");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.LOW);
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/tickets/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.companyId").value(10L))
                .andExpect(jsonPath("$.customerId").value(11L))
                .andExpect(jsonPath("$.assignedUserId").value(12L));
    }

    @Test
    void getTicket_failure() throws Exception {

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tickets/{id}", 1L))
                .andExpect(status().isNotFound());
    }

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

    @Test
    void updateTicket_success() throws Exception {

        Company company = new Company();
        company.setId(1L);
        Company newCompany = new Company();
        newCompany.setId(11L);

        Customer customer = new Customer();
        customer.setId(2L);
        Customer newCustomer = new Customer();
        newCustomer.setId(22L);

        User user = new User();
        user.setId(3L);
        User newUser = new User();
        newUser.setId(33L);

        Ticket ticket = new Ticket();

        ticket.setId(10L);
        ticket.setTitle("Test ticket");
        ticket.setDescription("Test description");
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(companyRepository.findById(11L)).thenReturn(Optional.of(newCompany));
        when(customerRepository.findById(22L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(33L)).thenReturn(Optional.of(newUser));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "Test ticket updated",
                "Test description updated",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                11L,
                22L,
                33L
        );

        mockMvc.perform(put("/api/tickets/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Test ticket updated"))
                .andExpect(jsonPath("$.description").value("Test description updated"))
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.companyId").value(11L))
                .andExpect(jsonPath("$.customerId").value(22L))
                .andExpect(jsonPath("$.assignedUserId").value(33L));
    }

    @Test
    void updateTicket_failure_noTicket() throws Exception {

        when(ticketRepository.findById(10L)).thenReturn(Optional.empty());

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "Test ticket",
                "Test description",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                11L,
                22L,
                33L
        );

        mockMvc.perform(put("/api/tickets/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTicket_failure_noCompany() throws Exception {

        Company oldCompany = new Company();
        oldCompany.setId(1L);

        Customer oldCustomer = new Customer();
        oldCustomer.setId(2L);
        Customer newCustomer = new Customer();
        newCustomer.setId(22L);

        User oldUser = new User();
        oldUser.setId(3L);
        User newUser = new User();
        newUser.setId(33L);

        Ticket ticket = new Ticket();

        ticket.setId(10L);
        ticket.setTitle("Test ticket");
        ticket.setDescription("Test description");
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCompany(oldCompany);
        ticket.setCustomer(oldCustomer);
        ticket.setAssignedUser(oldUser);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(companyRepository.findById(11L)).thenReturn(Optional.empty());
        when(customerRepository.findById(22L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(33L)).thenReturn(Optional.of(newUser));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "Test ticket updated",
                "Test description updated",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                11L,
                22L,
                33L
        );

        mockMvc.perform(put("/api/tickets/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTicket_failure_noCustomer() throws Exception {

        Company oldCompany = new Company();
        oldCompany.setId(1L);
        Company newCompany = new Company();
        newCompany.setId(11L);

        Customer oldCustomer = new Customer();
        oldCustomer.setId(2L);

        User oldUser = new User();
        oldUser.setId(33L);
        User newUser = new User();
        newUser.setId(33L);

        Ticket ticket = new Ticket();

        ticket.setId(10L);
        ticket.setTitle("Test ticket");
        ticket.setDescription("Test description");
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCompany(oldCompany);
        ticket.setCustomer(oldCustomer);
        ticket.setAssignedUser(oldUser);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(companyRepository.findById(11L)).thenReturn(Optional.of(newCompany));
        when(customerRepository.findById(22L)).thenReturn(Optional.empty());
        when(userRepository.findById(33L)).thenReturn(Optional.of(newUser));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "Test ticket updated",
                "Test description updated",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                11L,
                22L,
                33L
        );

        mockMvc.perform(put("/api/tickets/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTicket_failure_noUser() throws Exception {

        Company oldCompany = new Company();
        oldCompany.setId(1L);
        Company newCompany = new Company();
        newCompany.setId(11L);

        Customer oldCustomer = new Customer();
        oldCustomer.setId(2L);
        Customer newCustomer = new Customer();
        newCustomer.setId(22L);

        User oldUser = new User();
        oldUser.setId(3L);

        Ticket ticket = new Ticket();

        ticket.setId(10L);
        ticket.setTitle("Test ticket");
        ticket.setDescription("Test description");
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCompany(oldCompany);
        ticket.setCustomer(oldCustomer);
        ticket.setAssignedUser(oldUser);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(companyRepository.findById(11L)).thenReturn(Optional.of(newCompany));
        when(customerRepository.findById(22L)).thenReturn(Optional.of(newCustomer));
        when(userRepository.findById(33L)).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "Test ticket updated",
                "Test description updated",
                TicketPriority.LOW,
                TicketStatus.CLOSED,
                11L,
                22L,
                33L
        );

        mockMvc.perform(put("/api/tickets/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTicket_success() throws Exception {

        when(ticketRepository.existsById(10L)).thenReturn(true);

        mockMvc.perform(delete("/api/tickets/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(ticketRepository).deleteById(10L);
    }

    @Test
    void deleteTicket_failure_noTicket() throws Exception {

        when(ticketRepository.existsById(10L)).thenReturn(false);

        mockMvc.perform(delete("/api/tickets/{id}", 10L))
                .andExpect(status().isNotFound());

        verify(ticketRepository, never()).deleteById(any());
    }
}
