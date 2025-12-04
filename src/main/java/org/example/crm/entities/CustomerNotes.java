package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CustomerNotes {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CustomerNotes() {};

    public CustomerNotes(Customer customer, User user, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customer = customer;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    };
}
