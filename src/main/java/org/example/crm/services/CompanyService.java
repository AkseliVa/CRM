package org.example.crm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.mappers.CompanyMapper;
import org.example.crm.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(CompanyMapper::toDTO)
                .toList();
    }

    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(CompanyMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    @Transactional
    public CompanyDTO createCompany(CompanyCreateDTO dto) {
        Company company = CompanyMapper.fromCreateDTO(dto);
        Company saved = companyRepository.save(company);

        return CompanyMapper.toDTO(saved);
    }

    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyUpdateDTO dto) {
        Company existing = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        CompanyMapper.updateEntity(existing, dto);

        Company saved = companyRepository.save(existing);

        return CompanyMapper.toDTO(saved);
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found");
        }

        companyRepository.deleteById(id);
    }
}
