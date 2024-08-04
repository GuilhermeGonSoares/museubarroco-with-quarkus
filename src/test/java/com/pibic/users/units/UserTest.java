package com.pibic.users.units;

import com.pibic.users.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class UserTest {

    @Test
    public void ShouldCreateUser(){
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", true);
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void ShouldNotCreateUser(){
        assertThrows(IllegalArgumentException.class, () -> {
            User.create("John Doe",
                    "johndoe@email.com",
                    "123456", false);
        });
        }

}