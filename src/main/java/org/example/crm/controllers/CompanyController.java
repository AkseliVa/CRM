package org.example.crm.controllers;

import org.example.crm.entities.Company;
import org.example.crm.repositories.CompanyRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/api/company")
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/api/company/{id}")
    public Optional<Company> getCompany(@PathVariable Long id) {
        return companyRepository.findById(id);
    }

    @PostMapping("/api/company")
    public Company createCompany(@RequestBody Company company) {
        company.setCreated_at(LocalDateTime.now());
        company.setUpdated_at(LocalDateTime.now());
        return companyRepository.save(company);
    }

    @PutMapping("/api/company/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updatedCompany = companyRepository.findById(id).orElse(null);

        updatedCompany.setName(company.getName());
        updatedCompany.setIndustry(company.getIndustry());
        updatedCompany.setUpdated_at(LocalDateTime.now());

        return companyRepository.save(updatedCompany);
    }
}
