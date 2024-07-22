package com.pibic.tags.integrations;

import com.pibic.tags.Tag;
import com.pibic.tags.TagRepository;
import com.pibic.tags.TagServices;
import com.pibic.users.User;
import com.pibic.users.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    @TestTransaction
    public void setup() {
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    @TestTransaction
    public void ShouldReturnAllTags(){
        var user = createUser(true);
        tagServices.createTag("Java", user.getId());
        tagServices.createTag("Spring", user.getId());
        var allTags = tagServices.getAllTags();
        assertEquals(2, allTags.size());
        assertEquals("Java", allTags.get(0).name());
        assertEquals("Spring", allTags.get(1).name());

    }

    @Test
    @TestTransaction
    public void ShouldReturnTagByName(){
        var user = createUser(true);
        tagServices.createTag("Quarkus", user.getId());
        var tagResponse = tagServices.getTagByName("Quarkus");
        assertNotNull(tagResponse);
        assertEquals("Quarkus", tagResponse.name());
    }

    @Test
    @TestTransaction
    public void ShouldUpdateTagByAdmin(){
        var user = createUser(true);
        var tagResponse = tagServices.createTag("Java", user.getId());
        var updatedTag = tagServices.updateTag(tagResponse.id(), "Java 17", user.getId());
        assertNotNull(updatedTag);
        assertEquals("Java 17", updatedTag.name());
        assertEquals(tagResponse.id(), updatedTag.id());
    }

    @Test
    @TestTransaction
    public void ShouldUpdateTagUnPublishedByOwner(){
        var user = createUser(false);
        var tagResponse = tagServices.createTag("Spring", user.getId());
        var updatedTag = tagServices.updateTag(tagResponse.id(), "Quarkus", user.getId());
        assertNotNull(updatedTag);
        assertEquals("Quarkus", updatedTag.name());
        assertEquals(tagResponse.id(), updatedTag.id());
    }

    @Test
    @TestTransaction
    public void ShouldNotUpdateTagByOwnerWhenIsPublished(){
        var user = createUser(false);
        var tag = Tag.create("Java", user, true);
        tag.publish(true);
        tagRepository.persist(tag);
        assertThrows(IllegalArgumentException.class, () -> tagServices.updateTag(tag.getId(), "Quarkus", user.getId()));
    }

    @Test
    @TestTransaction
    public void ShouldNotUpdateTagByNonOwner(){
        var user = createUser(false);
        var tag = Tag.create("Java", user, true);
        tagRepository.persist(tag);
        var anotherUser = createUser(false);
        assertThrows(IllegalArgumentException.class, () -> tagServices.updateTag(tag.getId(), "Quarkus", anotherUser.getId()));
    }

    private User createUser(boolean isAdmin) {
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", isAdmin, true);
        userRepository.persist(user);
        return user;
    }
}
