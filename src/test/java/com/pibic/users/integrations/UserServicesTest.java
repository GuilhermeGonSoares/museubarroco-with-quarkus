package com.pibic.users.integrations;

import com.pibic.users.User;
import com.pibic.users.UserRepository;
import com.pibic.users.UserServices;
import com.pibic.users.dtos.RegisterUserDto;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServicesTest {

    @Inject
    UserServices userServices;
    @Inject
    UserRepository userRepository;

    @BeforeEach
    @TestTransaction
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @TestTransaction
    public void ShouldCreateUser(){
        var createUserDto = new RegisterUserDto(
                "John Doe",
                "johndoe@email.com",
                "123456"
        );
        var expected = userServices.registerUser(createUserDto);
        assertNotNull(expected);
        assertNotNull(expected.getId());
        assertEquals(expected.getName(), createUserDto.name());
        assertEquals(expected.getEmail(), createUserDto.email());
        assertFalse(expected.isAdmin());
    }


    @Test
    @TestTransaction
    public void ShouldNotCreateUserWithSameEmail() {
        var createUserDto = new RegisterUserDto(
                "Tainara",
                "tai@email.com",
                "123456"
        );
        var createUserDto2 = new RegisterUserDto(
                "Jane",
                "tai@email.com",
                "123456"
        );
        assertThrows(IllegalArgumentException.class, () -> {
            userServices.registerUser(createUserDto);
            userServices.registerUser(createUserDto2);
        });
    }

    @Test
    @TestTransaction
    public void ShouldListAllUsers() {
        var users = userServices.listAllUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
        userRepository.persist(User.create("Jane D",
                "jane@email.com",
                "123456", true));
        userRepository.persist(User.create("John Doe",
                "johndoe@email.com",
                "123456",  true));
        users = userServices.listAllUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    @TestTransaction
    public void ShouldReturnUserByEmail() {
        userRepository.persist(User.create("Tainara",
                "tai@email.com",
                "123456", true));
        User result = userServices.findByEmail("tai@email.com");
        assertNotNull(result);
        assertEquals("Tainara", result.getName());
        assertEquals("tai@email.com", result.getEmail());
        var result2 = userServices.findByEmail("guigo@email.com");
        assertNull(result2);
    }
}