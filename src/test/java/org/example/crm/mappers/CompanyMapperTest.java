package org.example.crm.mappers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.enums.CompanyIndustryType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompanyMapperTest {

    @Test
    public void testFromCreateDTO() {
        CompanyCreateDTO dto = new CompanyCreateDTO("testcompany", CompanyIndustryType.FINANCE);

        Company company = CompanyMapper.fromCreateDTO(dto);

        assertEquals("testcompany", company.getName());
        assertEquals(CompanyIndustryType.FINANCE, company.getIndustry());
        assertNotNull(company.getCreatedAt());
        assertNotNull(company.getUpdatedAt());
    }

    @Test
    public void testUpdateDTO() {
        Company company = new Company();

        company.setName("old name");
        company.setIndustry(CompanyIndustryType.FINANCE);
        company.setCreatedAt(LocalDateTime.now());

        CompanyUpdateDTO dto = new CompanyUpdateDTO("new name", CompanyIndustryType.MARKETING);

        CompanyMapper.updateEntity(company, dto);

        assertEquals("new name", company.getName());
        assertEquals(CompanyIndustryType.MARKETING, company.getIndustry());
        assertNotNull(company.getCreatedAt());
        assertNotNull(company.getUpdatedAt());
  }
}
