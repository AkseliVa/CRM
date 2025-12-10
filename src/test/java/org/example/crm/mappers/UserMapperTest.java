package org.example.crm.mappers;

import org.example.crm.DTOs.UserCreateDTO;
import org.example.crm.entities.User;
import org.example.crm.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
