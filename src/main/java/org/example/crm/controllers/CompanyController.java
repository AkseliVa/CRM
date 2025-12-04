package org.example.crm.controllers;

import org.example.crm.entities.Company;
import org.example.crm.repositories.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        company.setCreated_at(LocalDateTime.now());
        company.setUpdated_at(LocalDateTime.now());
        return companyRepository.save(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updatedCompany = companyRepository.findById(id).orElse(null);

        if (updatedCompany == null) {
            return ResponseEntity.notFound().build();
        }

        updatedCompany.setName(company.getName());
        updatedCompany.setIndustry(company.getIndustry());
        updatedCompany.setUpdated_at(LocalDateTime.now());

        return ResponseEntity.ok(companyRepository.save(updatedCompany));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {

        if (!companyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        companyRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
