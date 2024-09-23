package com.pibic.tags.units;

import com.pibic.tags.Tag;
import com.pibic.users.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TagTest {

    @Test
    public void ShouldCreateTag() {
        var tag = Tag.create("Java", createUser(true));
        assertEquals("Java", tag.getName());
        assertTrue(tag.isPublished());
    }

    @Test
    public void ShouldNotCreateTagWithNonUniqueName() {
        assertThrows(IllegalArgumentException.class, () -> Tag.create("Java", createUser(true)));
    }

    @Test
    public void ShouldCreateUnPublishedTagNonAdmin() {
        var tag = Tag.create("Java", createUser(false));
        assertEquals("Java", tag.getName());
        assertFalse(tag.isPublished());
    }

    @Test
    public void ShouldUpdateName()
    {
        var user = createUser(true);
        var tag = Tag.create("Java", user);
        assertEquals("Java 8", tag.getName());
    }

    @Test
    public void ShouldUpdateNameNonAdminWhenUnPublished()
    {
        var user = createUser(false);
        user.setId(1L);
        var tag = Tag.create("Java", user);
        tag.setId(1L);
        assertEquals("Java 8", tag.getName());
    }

    @Test
    public void ShouldNotUpdateNameNonAdminWhenPublished()
    {
        var user = createUser(false);
        var tag = Tag.create("Java", createUser(true));
    }

    @Test
    public void ShouldNotUpdateNameWithNonUniqueName()
    {
        var user = createUser(true);
        var tag = Tag.create("Java", user);
    }

    private User createUser(boolean isAdmin){
        var user = User.create(
                "GuiGo",
                "guigo@email.com",
                "123456",
                true
        );
        if (isAdmin)
            user.setAdmin(true);
        return user;
    }
}