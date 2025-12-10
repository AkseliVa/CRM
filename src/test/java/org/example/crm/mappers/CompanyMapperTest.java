package org.example.crm.mappers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.entities.Company;
import org.example.crm.enums.CompanyIndustryType;
import org.junit.jupiter.api.Test;

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
}
