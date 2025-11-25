package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.enums.TicketPriority;
import org.example.crm.enums.TicketStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="assigned_user_id")
    private User assignedUser;

    public Ticket() {};

    public Ticket(String title, String description, TicketPriority priority, TicketStatus status, LocalDateTime created_at, LocalDateTime updated_at) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    };
}
