package org.example.crm.DTOs;

public record CustomerUpdateDTO(
        String name,
        String email,
        String phone,
        Long companyId
) {
}
