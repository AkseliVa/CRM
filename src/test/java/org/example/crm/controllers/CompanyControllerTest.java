package org.example.crm.controllers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.entities.Company;
import org.example.crm.enums.CompanyIndustryType;
import org.example.crm.repositories.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyRepository companyRepository;

    @Test
    void getCompanies_success() throws Exception {

        Company company = new Company();

        company.setId(1L);
        company.setName("Company");
        company.setIndustry(CompanyIndustryType.FINANCE);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.findAll()).thenReturn(List.of(company));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Company"))
                .andExpect(jsonPath("$[0].industry").value("FINANCE"));
    }

    @Test
    void getCompany_success() throws Exception {

        Company company = new Company();

        company.setId(1L);
        company.setName("Company");
        company.setIndustry(CompanyIndustryType.IT);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Company"))
                .andExpect(jsonPath("$.industry").value("IT"));
    }

    @Test
    void getCompany_failure() throws Exception {

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCompany_success() throws Exception {

        Company company = new Company();

        company.setId(1L);
        company.setName("Test Company");
        company.setIndustry(CompanyIndustryType.FINANCE);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyCreateDTO dto = new CompanyCreateDTO(
                "Test Company",
                CompanyIndustryType.FINANCE
        );

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.industry").value("FINANCE"));
    }

    @Test
    void updateCompany_success() throws Exception {

        Company company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        company.setIndustry(CompanyIndustryType.FINANCE);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyUpdateDTO dto = new CompanyUpdateDTO(
                "Test Company Updated",
                CompanyIndustryType.IT
        );

        mockMvc.perform(put("/api/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company Updated"))
                .andExpect(jsonPath("$.industry").value("IT"));
    }

    @Test
    void updateCompany_failure_noCompany() throws Exception {

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        CompanyUpdateDTO dto = new CompanyUpdateDTO(
                "Test Company",
                CompanyIndustryType.FINANCE
        );

        mockMvc.perform(put("/api/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCompany_success() throws Exception {

        when(companyRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/companies/{id}", 1L))
        .andExpect(status().isNoContent());

        verify(companyRepository).deleteById(1L);
    }

    @Test
    void deleteCompany_failure_noCompany() throws Exception {

        when(companyRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/companies/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(companyRepository, never()).deleteById(any());
    }
}
