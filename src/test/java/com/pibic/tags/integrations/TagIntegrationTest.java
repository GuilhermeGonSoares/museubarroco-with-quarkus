package com.pibic.tags.integrations;

import com.pibic.tags.TagRepository;
import com.pibic.tags.TagServices;
import com.pibic.users.User;
import com.pibic.users.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TagIntegrationTest {

    @Inject
    TagServices tagServices;
    @Inject
    TagRepository tagRepository;
    @Inject
    UserRepository userRepository;

    @Test
    @TestTransaction
    public void ShouldCreateTag() {
        var user = createUser(true);
        var tagResponse = tagServices.createTag("Java", user.getId());
        assertNotNull(tagResponse);
        assertNotNull(tagResponse.id());
        assertEquals("Java", tagResponse.name());
        var tag = tagRepository.findById(tagResponse.id());
        assertNotNull(tag);
        assertEquals(tag.getId(), tagResponse.id());
        assertEquals("Java", tag.getName());
        assertTrue(tag.isPublished());
    }

    @Test
    public void ShouldNotCreateTagWhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> tagServices.createTag("Java",  100L));
    }

    @Test
    @TestTransaction
    public void ShouldNotCreateTagWhenNameIsNotUnique() {
        var user = createUser(true);
        tagServices.createTag("Java-not-unique", user.getId());
        assertThrows(IllegalArgumentException.class, () -> tagServices.createTag("Java-not-unique", user.getId()));
    }

    private User createUser(boolean isAdmin) {
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", isAdmin, true);
        userRepository.persist(user);
        return user;
    }
}
