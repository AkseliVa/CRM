package org.example.crm.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.crm.enums.CompanyIndustryType;

public record CompanyUpdateDTO(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotNull(message = "Industry cannot be empty")
        CompanyIndustryType industry
) {
}
