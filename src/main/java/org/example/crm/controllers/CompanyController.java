package org.example.crm.controllers;

import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.mappers.CompanyMapper;
import org.example.crm.repositories.CompanyRepository;
import org.example.crm.services.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyDTO> getCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyCreateDTO dto) {
        CompanyDTO created = companyService.createCompany(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateDTO dto) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
