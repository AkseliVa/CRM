package org.example.crm.controllers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.mappers.CompanyMapper;
import org.example.crm.repositories.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public List<CompanyDTO> getCompanies() {
        return companyRepository.findAll().stream()
                .map(CompanyMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(c -> ResponseEntity.ok(CompanyMapper.toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyCreateDTO dto) {
        Company created = companyRepository.save(CompanyMapper.fromCreateDTO(dto));
        return ResponseEntity.ok(CompanyMapper.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateDTO dto) {
        return companyRepository.findById(id)
                .map(existing -> {
                    CompanyMapper.updateEntity(existing, dto);
                    Company saved = companyRepository.save(existing);
                    return ResponseEntity.ok(CompanyMapper.toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
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
