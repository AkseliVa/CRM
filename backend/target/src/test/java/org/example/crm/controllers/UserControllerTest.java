package org.example.crm.controllers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;
import org.example.crm.enums.UserRole;
import org.example.crm.exceptions.ResourceNotFoundException;
import org.example.crm.repositories.UserRepository;
import org.example.crm.services.UserService;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_success() throws Exception {
        UserDTO dto = new UserDTO(
                1L,
                "user@email.com",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.getAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("user@email.com"))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }

    @Test
    void getUser_success() throws Exception {
        UserDTO dto = new UserDTO(
                1L,
                "user@email.com",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.getUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getUser_failure() throws Exception {
        when(userService.getUserById(1L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_success() throws Exception {
        UserCreateDTO createDto = new UserCreateDTO(
                "user@email.com",
                "password",
                UserRole.USER
        );
        UserDTO resultDto = new UserDTO(
                1L,
                "user@email.com",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void updateUser_success() throws Exception {
        UserUpdateDTO updateDto = new UserUpdateDTO(
                "user@email.com",
                "password",
                UserRole.USER
        );
        UserDTO resultDto = new UserDTO(
                1L,
                "user@email.com",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(resultDto);

        mockMvc.perform(put("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void deleteUser_success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                        .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_failure_noUser() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L));
    }
}
