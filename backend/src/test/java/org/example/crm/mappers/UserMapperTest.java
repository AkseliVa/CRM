package org.example.crm.mappers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.DTOs.UserUpdateDTO;
import org.example.crm.entities.User;
import org.example.crm.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Test
    void testFromCreateDTO() {
        UserCreateDTO dto = new UserCreateDTO("test@test.com", "goodpassword1", UserRole.ADMIN);

        User user = UserMapper.fromCreateDTO(dto);

        assertEquals("test@test.com", user.getEmail());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    public void testUpdateDTO() {
        User user = new User();

        user.setEmail("old email");
        user.setRole(UserRole.USER);
        user.setPasswordHash("old password");
        user.setCreatedAt(LocalDateTime.now());

        UserUpdateDTO dto = new UserUpdateDTO("new email", "new password", UserRole.ADMIN);

        UserMapper.updateEntity(user, dto);

        assertEquals("new email", user.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotEquals("new password", user.getPasswordHash());
        assertTrue(encoder.matches("new password", user.getPasswordHash()));

        assertEquals(UserRole.ADMIN, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }
}
