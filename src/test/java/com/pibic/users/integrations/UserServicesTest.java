package com.pibic.users.integrations;

import com.pibic.users.User;
import com.pibic.users.UserRepository;
import com.pibic.users.UserServices;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", true, true);
        var expected = userServices.createUser(user.getName(), user.getEmail(), user.getPassword(), user.isAdmin());
        assertNotNull(expected);
        assertNotNull(expected.getId());
        assertEquals(expected.getName(), user.getName());
        assertEquals(expected.getEmail(), user.getEmail());
        assertTrue(expected.isAdmin());
    }

    @Test
    @TestTransaction
    public void ShouldCreateNonAdminUser() {
        var user = User.create("Jane D",
                "jane@email.com",
                "123456", false, true);
        var expected = userServices.createUser(user.getName(), user.getEmail(), user.getPassword(), user.isAdmin());
        assertNotNull(expected);
        assertNotNull(expected.getId());
        assertEquals(expected.getName(), user.getName());
        assertEquals(expected.getEmail(), user.getEmail());
        assertFalse(expected.isAdmin());
    }

    @Test
    @TestTransaction
    public void ShouldNotCreateUserWithSameEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            userServices.createUser("Tainara", "jane@email.com","123456",false);
            userServices.createUser("Jane", "jane@email.com","123456",false);
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
                "123456", false, true));
        userRepository.persist(User.create("John Doe",
                "johndoe@email.com",
                "123456", true, true));
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
                "123456", false, true));
        User result = userServices.findByEmail("tai@email.com");
        assertNotNull(result);
        assertEquals("Tainara", result.getName());
        assertEquals("tai@email.com", result.getEmail());
        var result2 = userServices.findByEmail("guigo@email.com");
        assertNull(result2);
    }
}