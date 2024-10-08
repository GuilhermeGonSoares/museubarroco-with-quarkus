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
public class TagServiceTest {

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
        var allTags = tagServices.getAllPublishedTags();
        assertEquals(0, allTags.size());
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
    public void ShouldAdminDeleteTag(){
        var user = createUser(true);
        var tagResponse = tagServices.createTag("Java", user.getId());
        tagServices.deleteTag(tagResponse.id(), user.getId());
        assertNull(tagRepository.findById(tagResponse.id()));
    }

    @Test
    @TestTransaction
    public void ShouldAdminCanDeletePublishedTag(){
        var user = createUser(false);
        var admin = createUser(true);
        var tag = Tag.create("Java", user);
        tag.publish();
        tagRepository.persist(tag);
        assertThrows(IllegalArgumentException.class, () -> tagServices.deleteTag(tag.getId(), user.getId()));
        tagServices.deleteTag(tag.getId(), admin.getId());
        assertNull(tagRepository.findById(tag.getId()));
    }

    @Test
    @TestTransaction
    public void ShouldOwnerDeleteTag(){
        var user = createUser(false);
        var anotherUser = createUser(false);
        var tagResponse = tagServices.createTag("Java", user.getId());
        assertThrows(IllegalArgumentException.class, () -> tagServices.deleteTag(tagResponse.id(), anotherUser.getId()));
        tagServices.deleteTag(tagResponse.id(), user.getId());
        assertNull(tagRepository.findById(tagResponse.id()));
    }

    private User createUser(boolean isAdmin) {
        var user = User.create("John Doe",
                "johndoe@email.com",
                "123456", true);
        if (isAdmin) {
            user.setAdmin(true);
        }
        userRepository.persist(user);
        return user;
    }
}
