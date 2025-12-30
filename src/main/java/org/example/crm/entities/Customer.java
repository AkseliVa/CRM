package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
