package org.example.crm.controllers;

import org.example.crm.DTOs.CompanyCreateDTO;
import org.example.crm.DTOs.CompanyDTO;
import org.example.crm.DTOs.CompanyUpdateDTO;
import org.example.crm.enums.CompanyIndustryType;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.services.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

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
    private CompanyService companyService;

    @Test
    void getCompanies_success() throws Exception {
        CompanyDTO dto = new CompanyDTO(1L,
                "Company",
                CompanyIndustryType.FINANCE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyService.getAllCompanies()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Company"))
                .andExpect(jsonPath("$[0].industry").value("FINANCE"));
    }

    @Test
    void getCompany_success() throws Exception {
        CompanyDTO dto = new CompanyDTO(1L,
                "Company",
                CompanyIndustryType.IT,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyService.getCompanyById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Company"))
                .andExpect(jsonPath("$.industry").value("IT"));
    }

    @Test
    void getCompany_failure() throws Exception {
        when(companyService.getCompanyById(1L))
                .thenThrow(new ResourceNotFoundException("Company not found"));

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCompany_success() throws Exception {
        CompanyCreateDTO createDto = new CompanyCreateDTO(
                "Test Company",
                CompanyIndustryType.FINANCE
        );
        CompanyDTO resultDto = new CompanyDTO(
                1L,
                "Test Company",
                CompanyIndustryType.FINANCE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyService.createCompany(any(CompanyCreateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.industry").value("FINANCE"));
    }

    @Test
    void updateCompany_success() throws Exception {
        CompanyUpdateDTO updateDto = new CompanyUpdateDTO(
                "Test Company Updated",
                CompanyIndustryType.IT
        );
        CompanyDTO resultDto = new CompanyDTO(
                1L,
                "Updated name",
                CompanyIndustryType.FINANCE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyService.updateCompany(eq(1L), any(CompanyUpdateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.industry").value("FINANCE"));
    }

    @Test
    void deleteCompany_success() throws Exception {
        doNothing().when(companyService).deleteCompany(1L);

        mockMvc.perform(delete("/api/companies/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCompany_failure_noCompany() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(companyService).deleteCompany(1L);

        mockMvc.perform(delete("/api/companies/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
