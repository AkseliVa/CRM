package org.example.crm.DTOs;

import org.example.crm.enums.CompanyIndustryType;

public record CompanyCreateDTO(
        String name,
        CompanyIndustryType industry
) {
}
