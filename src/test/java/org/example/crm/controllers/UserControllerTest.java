package org.example.crm.controllers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;
import org.example.crm.enums.UserRole;
import org.example.crm.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Test
    void getUsers_success() throws Exception {
        User user = new User();

        user.setId(1L);
        user.setEmail("user@email.com");
        user.setPasswordHash("password");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("user@email.com"))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }

    @Test
    void getUser_success() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setEmail("user@email.com");
        user.setPasswordHash("password");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getUser_failure() throws Exception {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_success() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setEmail("user@email.com");
        user.setPasswordHash("password");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserCreateDTO dto = new UserCreateDTO(
                "user@email.com",
                "password",
                UserRole.USER
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void updateUser_success() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setEmail("test@email.com");
        user.setPasswordHash("password");
        user.setRole(UserRole.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserUpdateDTO dto = new UserUpdateDTO(
                "updated@email.com",
                "newpassword",
                UserRole.ADMIN
        );

        mockMvc.perform(put("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void updateUser_failure_noUser() throws Exception {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserUpdateDTO dto = new UserUpdateDTO(
                "email",
                "password",
                UserRole.USER
        );

        mockMvc.perform(put("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_success() throws Exception {

        when(userRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_failure_noUser() throws Exception {

        when(userRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(userRepository, never()).deleteById(any());
    }
}
