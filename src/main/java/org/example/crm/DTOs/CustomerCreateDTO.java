package org.example.crm.DTOs;

public record CustomerCreateDTO(
        String name,
        String email,
        String phone,
        Long companyId
) {
}
