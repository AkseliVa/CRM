package org.example.crm.DTOs;

import org.example.crm.enums.CompanyIndustryType;

public record CompanyUpdateDTO(
        String name,
        CompanyIndustryType industry
) {
}
