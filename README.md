# CRM

This is the backend repository for a CRM-application that I am building/built.<br>
The purpose of this application is purely educational.<br>
After my studies, to keep learning and increasing my chances of landing a job in the software development industry, I decided to start building my own applications and learning concepts not taught in school

## Technologies

- Java
- Spring Boot
- PostgreSQL

## Entities, DTOs, Mappers, Repositories and Controllers

The applications funcionalities consist of these entities and their respectful DTOs, Mappers, Repositories and Controllers.

1. Company
   - This is the "customer company". A company contains customers and tickets
3. Customer
   - This is a person working at a "customer company". The entities contain basic contact-info.
5. CustomerNotes
   - The CustomerNotes entity might have been a bit unnecessary. The idea and point was that the employees of the company using the application have a way to leave notes for themselves and others about a specific customer, timelines, stuff agreed upon etc.
7. Ticket
   - A ticket is associated to a customer in a customer company. These are your standard CRM-tickets with the customer, priorities, status, contents and assigned users.
9. User
    - A user is the person actually using the software and creating and completing tickets. There are currently two user types; user and admin.
  
Each of the above mentioned entites have their own DTOs and Mappers. The DTOs are divided by the type of API-request.
So each entity has a "xxxDTO", "xxxCreateDTO" and "xxxUpdateDTO".
The Create- and UpdateDTOs are currently identical for each entity, but the reason behind using them is purely learning purposes.

The Mappers transform the DTOs from and to applicable database entitites, so that, for example, a users password is never sent via the API.

Here is an example of a Mapper-class.

```
package org.example.crm.mappers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.entities.Customer;
import org.example.crm.entities.Ticket;
import org.example.crm.entities.User;

import java.time.LocalDateTime;

public class TicketMapper {

    public static TicketDTO toTicketDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCompany().getId(),
                ticket.getCustomer().getId(),
                ticket.getAssignedUser().getId(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    public static Ticket fromCreateDTO(TicketCreateDTO dto, Company company, Customer customer, User user) {
        Ticket ticket = new Ticket();

        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setPriority(dto.priority());
        ticket.setStatus(dto.status());
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticket;
    }

    public static void updateEntity(TicketUpdateDTO dto, Ticket ticket, Company company, Customer customer, User user) {
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setPriority(dto.priority());
        ticket.setStatus(dto.status());
        ticket.setCompany(company);
        ticket.setCustomer(customer);
        ticket.setAssignedUser(user);
        ticket.setUpdatedAt(LocalDateTime.now());
    }
}

```

The Controllers contain the basic GET, POST, PUT and DELETE endpoints for basic CRUD-functionality.

Here is an example of a controller-class

```
package org.example.crm.controllers;

import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Ticket;
import org.example.crm.mappers.TicketMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.TicketRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, CompanyRepository companyRepository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    };

    @GetMapping
    public List<TicketDTO> getTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toTicketDTO)
                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(t -> ResponseEntity.ok(TicketMapper.toTicketDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    };

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket (@RequestBody TicketCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                .orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        var customer = customerRepository.findById(dto.customerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.assignedUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = TicketMapper.fromCreateDTO(dto, company, customer, user);

        Ticket saved = ticketRepository.save(ticket);

        return ResponseEntity.ok(TicketMapper.toTicketDTO(saved));
    };

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketUpdateDTO dto) {
        Ticket existing = ticketRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        var company = companyRepository.findById(dto.companyId())
                .orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        var customer = customerRepository.findById(dto.customerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(dto.assignedUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        TicketMapper.updateEntity(dto, existing, company, customer, user);

        Ticket saved = ticketRepository.save(existing);

        return ResponseEntity.ok(TicketMapper.toTicketDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {

        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ticketRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

```

## Tests

Each entity has their designated test files for functionalitites.
Currently the tests cover the Mapper-functionality and most of the Controller-functionality with succeeding and failing tests for each endpoint of each controller.

```
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



```

The current test coverage is shown in the picture below.

![Test coverage](/src/main/resources/static/testcoverage.png)
