package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerNote> notes = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    public Customer() {};

    public Customer(Company company, String name, String email, String phone, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.company = company;
      this.name = name;
      this.email = email;
      this.phone = phone;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    };
}
