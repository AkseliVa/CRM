package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.enums.CompanyIndustryType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CompanyIndustryType industry;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    public Company() {};

    public Company(String name, CompanyIndustryType industry, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.industry = industry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    };

    public Company(String name, CompanyIndustryType industry) {
        this.name = name;
        this.industry = industry;
    }
}
