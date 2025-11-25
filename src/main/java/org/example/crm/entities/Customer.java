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

    private String name;

    private String email;

    private String phone;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public Customer() {};

    public Customer(Company company, String name, String email, String phone, LocalDateTime created_at, LocalDateTime updated_at) {
      this.company = company;
      this.name = name;
      this.email = email;
      this.phone = phone;
      this.created_at = created_at;
      this.updated_at = updated_at;
    };
}
