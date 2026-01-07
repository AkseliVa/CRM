package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CustomerNote {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String content;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CustomerNote() {};

    public CustomerNote(Customer customer, User user, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customer = customer;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    };
}
