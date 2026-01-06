package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.crm.enums.CompanyIndustryType;

public record CompanyCreateDTO(
        @NotBlank(message = "Company should have a name")
        String name,

        @NotNull(message = "Company should have an industry")
        CompanyIndustryType industry
) {
}
