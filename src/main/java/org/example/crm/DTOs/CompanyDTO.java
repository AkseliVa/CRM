package org.example.crm.DTOs;

import org.example.crm.enums.CompanyIndustryType;

import java.time.LocalDateTime;

public record CompanyDTO(
        Long id,
        String name,
        CompanyIndustryType industry,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
