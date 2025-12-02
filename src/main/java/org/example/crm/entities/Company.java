package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.enums.CompanyIndustryType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CompanyIndustryType industry;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public Company() {};

    public Company(String name, CompanyIndustryType industry, LocalDateTime created_at, LocalDateTime updated_at) {
        this.name = name;
        this.industry = industry;
        this.created_at = created_at;
        this.updated_at = updated_at;
    };

    public Company(String name, CompanyIndustryType industry) {
        this.name = name;
        this.industry = industry;
    }
}
