# CRM

This is the backend repository for a CRM-application that I am building/built.<br>
The purpose of this application is purely educational.<br>
After my studies, to keep learning and increasing my chances of landing a job in the software development industry, I decided to start building my own applications and learning concepts not taught in school

## Technologies

- Java
- Spring Boot
- PostgreSQL

## Entities, DTOs, Mappers, Repositories, Controllers and Services

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

import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public List<TicketDTO> getTickets() {
        return ticketService.getAllTickets();
    };

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    };

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket (@RequestBody TicketCreateDTO dto) {
        TicketDTO created = ticketService.createTicket(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}


```

The Services contain the logic in the application. They do the actual transforming of entities to DTOs and vice versa. They are also responsible for checking that an incoming DTO's values are correct.

Here is an example of a service class

```

package org.example.crm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.TicketCreateDTO;
import org.example.crm.DTOs.TicketDTO;
import org.example.crm.DTOs.TicketUpdateDTO;
import org.example.crm.entities.Ticket;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.mappers.TicketMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.repositories.CustomerRepository;
import org.example.crm.repositories.TicketRepository;
import org.example.crm.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAllWithRelations().stream()
                .map(TicketMapper::toTicketDTO)
                .toList();
    }

    public TicketDTO getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toTicketDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Transactional
    public TicketDTO createTicket(TicketCreateDTO dto) {
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Ticket ticket = TicketMapper.fromCreateDTO(dto, company, customer, user);
        Ticket saved = ticketRepository.save(ticket);

        return TicketMapper.toTicketDTO(saved);
    }

    @Transactional
    public TicketDTO updateTicket(Long id, TicketUpdateDTO dto) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        var company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        var customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        var user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TicketMapper.updateEntity(dto, existing, company, customer, user);

        Ticket saved = ticketRepository.save(existing);

        return TicketMapper.toTicketDTO(saved);
    }

    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        ticketRepository.deleteById(id);
    }
}


```
# Tests

Each entity has their designated test files for functionalitites.
Currently the tests cover the Mapper-functionality and most of the Controller-functionality with succeeding and failing tests for each endpoint of each controller.

```
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




```

The current test coverage is shown in the picture below.

![Test coverage](/src/main/resources/static/testcoverage.png)
