package org.example.crm.mappers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;

import java.time.LocalDateTime;

public class CompanyMapper {

    public static CompanyDTO toDTO(Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }

    public static Company fromCreateDTO(CompanyCreateDTO dto) {
        Company company = new Company();

        company.setName(dto.name());
        company.setIndustry(dto.industry());
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return company;
    }

    public static void updateEntity(Company company, CompanyUpdateDTO dto) {
        company.setName(dto.name());
        company.setIndustry(dto.industry());
        company.setUpdatedAt(LocalDateTime.now());
    }
}
